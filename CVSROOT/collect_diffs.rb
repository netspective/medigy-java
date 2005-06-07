#!/usr/bin/ruby -w

# Part of CVSspam
#   http://www.badgers-in-foil.co.uk/projects/cvsspam/
# Copyright (c) David Holroyd

#  CVSROOT is /var/lib/cvs
#  ARGV is 'CVSROOT/foo/space dir space file,NONE,1.1 some,file,1.4,1.5'
#  ----
#  Update of /var/lib/cvs/CVSROOT/foo/space dir
#  In directory foil:/tmp/cvs-serv13059
#  
#  Modified Files:
#  	some,file 
#  Added Files:
#  	space file 
#  Log Message:
#  msg
#  


# Assumptions
# - file names do not contain newlines or single quotes


$tmpdir = ENV["TMPDIR"] || "/tmp"
$dirtemplate = "#cvsspam.#{Process.getpgrp}.#{Process.uid}"

def find_data_dir
  Dir["#{$tmpdir}/#{$dirtemplate}-*"].each do |dir|
    stat = File.stat(dir)
    return dir if stat.owned?
  end
  nil
end


def blah(msg)
  if $debug
    $stderr.puts "collect_diffs.rb: #{msg}"
  end
end


# Like IO.popen, but accepts multiple arguments like Kernel.exec
# (So no need to escape shell metacharacters)
def safer_popen(*args)
  IO.popen("-") do |pipe|
    if pipe==nil
      exec(*args)
    else
      yield pipe
    end
  end
end

class ChangeInfo
  def initialize(file, fromVer, toVer)
    @file, @fromVer, @toVer = file, fromVer, toVer
    if fromVer == toVer
      fail "'from' and 'to' versions should be different ('#{fromVer}')"
    end
  end
  attr_reader :file, :fromVer, :toVer
  def to_s
    "<ChangeInfo \"#{@file}\" #{@toVer}<--#{@fromVer}>"
  end

  def isAddition ; fromVer == 'NONE' end

  def isRemoval ; toVer == 'NONE' end

  #def isModification ; !(isAddition || isRemoval) end
end

$commitinfo_tags = nil

def get_commitinfo_tag(filename)
  if $commitinfo_tags.nil?
    return nil unless FileTest.exists?("#{$datadir}/commitinfo-tags")
    File.open("#{$datadir}/commitinfo-tags") do |file|
      $commitinfo_tags = Hash.new
      file.each_line do |line|
	line =~ /([^\t]+)\t(.+)/
	key = $2
	val = $1
	key.sub!(/^#{ENV['CVSROOT']}\//, '')
	$commitinfo_tags[key] = val
      end
    end
  end
  return $commitinfo_tags[filename]
end


# cvs_info comes from the command line, ultimately as the expansion of the
# %{sVv} in $CVSROOT/loginfo.  It isn't possible to parse this value
# unambiguously, but we make an effort to get it right in as many cases as
# possible.
def collect_antique_style_args(cvs_info)
  # remove leading slashes that may appear due to the user entering trailing
  # slashes in their CVSROOT specification
  cvs_info.sub!(/^\/+/, "")

  unless cvs_info.slice(0, $repository_path.length+1) == "#{$repository_path} "
    fail "calculated repository path ('#{$repository_path}') doesn't match start of command line arg ('#{cvs_info}')"
  end

  version_info = cvs_info.slice($repository_path.length,
                                cvs_info.length-$repository_path.length)

  changes = Array.new
  # make a list of changed files given on the command line
  while version_info.length>0
    if version_info.sub!(/^ (.+?),(NONE|[.0-9]+),(NONE|[.0-9]+)/, '') == nil
      fail "'#{version_info}' doesn't match ' <name>,<ver>,<ver> ...'"
    end
    changes << ChangeInfo.new($1, $2, $3)
  end

  return changes
end


def collect_modern_style_args(cvs_info)
#  unless cvs_info[0] == $repository_path
#    fail "calculated repository path ('#{$repository_path}') doesn't match first command line arg ('#{cvs_info[0]}')"
#  end
  changes = Array.new
  i = 0
  while i < cvs_info.length
    changes << ChangeInfo.new(cvs_info[i], cvs_info[i+=1], cvs_info[i+=1])
    i+=1
  end
  return changes
end

# Replace multiple adjecent forward slashes with a single slash.
def sanitise_path(path)
  path.gsub(/\/+/, "/")
end

def process_log(cvs_info)
  cvsroot = sanitise_path(ENV['CVSROOT'])

  $datadir = find_data_dir()

  raise "missing data dir (#{$tmpdir}/#{$dirtemplate}-XXXXXX)" if $datadir==nil

  line = $stdin.gets
  unless line =~ /^Update of (.+)/
    fail "Log preamble looks suspect (doesn't start 'Update of ...')"
  end

  $path = sanitise_path($1)
  unless $path.slice(0,cvsroot.length) == cvsroot
    fail "CVSROOT ('#{cvsroot}') doesn't match log preamble ('#{$path}')"
  end

  $repository_path = $path.slice(cvsroot.length+1, $path.length-cvsroot.length-1)

  if $use_modern_argument_list
    changes = collect_modern_style_args(cvs_info)
  else
    changes = collect_antique_style_args(cvs_info)
  end

  # look for the start of the user's comment
  $stdin.each do |line|
    break if line =~ /^Log Message/
  end

  unless line =~ /^Log Message/
    fail "Input did not contain a 'Log Message:' entry"
  end

  File.open("#{$datadir}/logfile", File::WRONLY|File::CREAT|File::APPEND) do |file|
    $stdin.each do |line|
      # remove any trailing whitespace; we don't want the split() below to
      # produce empty trailing items due to '\r' at the end of the line
      # (if input is 'DOS' style),
      line.sub!(/\s*$/, "")
      
      # 'Mac' clients sending logs to a unix server may denote end-of-line with
      # a carriage-return, defeating the $stdin.each above (i.e. the whole log
      # message will appear as a single line containing '\r's).  We handle this
      # case explicitly here, so that cvsspam.rb's Subject header generation
      # doesn't break,
      line.split(/\r/).each do |part|
        file.puts "#> #{part}"
      end
    end

    changes.each do |change|

      # record version information
      file.puts "#V #{change.fromVer},#{change.toVer}"

      # note if the file is on a branch
      tag = nil
      if change.isRemoval
	tag = get_commitinfo_tag("#{$repository_path}/#{change.file}")
      else
        status = nil
        safer_popen($cvs_prog, "-nq", "status", change.file) do |io|
          status = io.read
        end
        fail "couldn't get cvs status: #{$!} (exited with #{$?})" unless ($?>>8)==0

	if status =~ /^\s*Sticky Tag:\s*(.+) \(branch: +/m
	  tag = $1
	end
      end
      file.puts "#T #{tag}" unless tag.nil?

      diff_cmd = Array.new << $cvs_prog << "-nq" << "diff" << "-Nu"
      diff_cmd << "-kk" if $diff_ignore_keywords

      if change.isAddition
        file.write "#A "
        # cruft up a date in the distant past, when the file would not have
        # existed, so that the diff will show all lines as added
        diff_cmd << "-D1/26/1977" << "-r#{change.toVer}"
      elsif change.isRemoval
        file.write "#R "
        # just specifying one version, cvs will diff between that version and
        # the current version (will show all lines removed)
        diff_cmd << "-r#{change.fromVer}"
      else
        file.write "#M "
        diff_cmd << "-r#{change.fromVer}" << "-r#{change.toVer}"
      end
      file.puts "#{$repository_path}/#{change.file}"
      diff_cmd << change.file
      # do a cvs diff and place the output into our temp file
      blah("about to run #{diff_cmd.join(' ')}")
      safer_popen(*diff_cmd) do |pipe|
        # skip over cvs-diff's preamble
        pipe.each do |line|
          break if line =~ /^diff /
        end
        file.puts "#U #{line}"
        pipe.each do |line|
          file.puts "#U #{line}"
        end
      end
      # TODO: don't how to do this reliably on different systems...
      #fail "cvsdiff did not give exit status 1 for invocation: #{diff_cmd.join(' ')}" unless ($?>>8)==1
    end
  end

end


# sometimes, CVS would exit with an error like,
# 
#   cvs [server aborted]: received broken pipe signal
# 
# consuming all the data on our standard input seems to stop this error
# happening.  (This problem may have been fixed in CVS 1.12.6, looking at
# a message in the NEWS file.)
def consume_stdin()
  $stdin.read()
end


def mailtest
  lastdir = nil
  File.open("#{$datadir}/lastdir") do |file|
    lastdir = sanitise_path(file.gets)
  end
  if $path == lastdir
    blah("sending spam.  (I am #{$0})")
    # REVISIT: $0 will not contain the path to this script on all systems
    cmd = File.dirname($0) + "/cvsspam.rb"
    unless system(cmd, "#{$datadir}/logfile", *$passthroughArgs)
      fail "problem running '#{cmd}'"
    end
    if $debug
      blah("leaving file #{$datadir}/logfile")
    else 
      File.unlink("#{$datadir}/logfile")
    end
    File.unlink("#{$datadir}/lastdir")
    File.unlink("#{$datadir}/commitinfo-tags") if FileTest.exists?("#{$datadir}/commitinfo-tags")
    Dir.rmdir($datadir) unless $debug
  else
    blah("not spam time yet, #{$path}!=#{lastdir}")
  end
end


class CVSConfig
  def initialize(filename)
    @data = Hash.new
    File.open(filename) do |io|
      read(io)
    end
  end

  def read(io)
    io.each do |line|
      parse_line(line)
    end
  end

  def parse_line(line)
    # strip any comment (assumes values can't contain '#')
    line.sub!(/#.*$/, "")
    if line =~ /^\s*(.*?)\s*=\s*(.*?)\s*$/
      @data[$1] = $2
    end
  end

  def [](key)
    @data[key]
  end
end

$config = nil
$cvs_prog = "cvs"
$debug = false
$diff_ignore_keywords = false
$task_keywords = []

unless ENV.has_key?('CVSROOT')
  fail "$CVSROOT not defined.  It should be when I am invoked from CVSROOT/loginfo"
end


def handle_operation?(args)
  # The CVS 1.12.x series pass an argument with the value "- New directory"
  # whereas previous versions passed "some/path - New directory".  The newer
  # syntax looks like a command-line switch, and confuses GetOpt.  We check
  # for that case before GetOpt processing
  unless ARGV.detect{|el| el =~ /- New directory$/}.nil?
    blah("No action taken on directory creation")
    return false
  end
  unless ARGV.detect{|el| el =~ /- Imported sources$/}.nil?
    blah("Imported not handled")
    return false
  end
  return true
end


unless handle_operation?(ARGV)
  consume_stdin()
  exit
end


require 'getoptlong'

opts = GetoptLong.new(
  [ "--to",     "-t", GetoptLong::REQUIRED_ARGUMENT ],
  [ "--config", "-c", GetoptLong::REQUIRED_ARGUMENT ],
  [ "--debug",  "-d", GetoptLong::NO_ARGUMENT ],
  [ "--from",   "-u", GetoptLong::REQUIRED_ARGUMENT ]
)

# arguments to pass though to 'cvsspam.rb'
$passthroughArgs = Array.new
opts.each do |opt, arg|
  if ["--to", "--config", "--from"].include?(opt)
    $passthroughArgs << opt << arg
  end
  if ["--debug"].include?(opt)
    $passthroughArgs << opt
  end
  $config = arg if opt=="--config"
  $debug = true if opt == "--debug"
end

blah("CVSROOT is #{ENV['CVSROOT']}")
blah("ARGV is <#{ARGV.join('>, <')}>")

cvsroot_dir = "#{ENV['CVSROOT']}/CVSROOT"

if $config == nil
  if FileTest.exists?("#{cvsroot_dir}/cvsspam.conf")
    $config = "#{cvsroot_dir}/cvsspam.conf"
  elsif FileTest.exists?("/etc/cvsspam/cvsspam.conf")
    $config = "/etc/cvsspam/cvsspam.conf"
  end

  if $config != nil
    $passthroughArgs << "--config" << $config
  end
end


$use_modern_argument_list = false

cvs_config_filename = "#{cvsroot_dir}/config"

if FileTest.exists?(cvs_config_filename)
  cvs_config = CVSConfig.new(cvs_config_filename)

  $use_modern_argument_list = cvs_config["UseNewInfoFmtStrings"] == "yes"
end

if $config != nil
  if FileTest.exists?($config)
    def addHeader(name,val)
    end
    def addRecipient(who)
    end
    class GUESS
    end
    load $config
  else
    blah("Config file '#{$config}' not found, ignoring")
  end
end

if $use_modern_argument_list
  if ARGV.length % 3 != 0
    $stderr.puts "Expected 3 arguments for each file"
  end
  process_log(ARGV)
else
  if ARGV.length != 1
    $stderr.puts "Expected arguments missing"
    $stderr.puts "* You shouldn't run collect_diffs by hand, but from a CVSROOT/loginfo entry *"
    $stderr.puts "Usage: collect_diffs.rb [ --to <email> ] [ --config <file> ] %{sVv}"
    $stderr.puts "       (the sequence '%{sVv}' is expanded by CVS, when found in CVSROOT/loginfo)"
    exit
  end
  process_log(ARGV[0])
end
mailtest
