# Class: java7
#
# This module manages Oracle Java7
# Parameters: none
# Requires:
#  apt
# Sample Usage:
#  include java7
class java7 {
  include apt

  apt::ppa { "ppa:webupd8team/java": }

  file { '/tmp/java.preseed':
    source => 'puppet:///modules/java7/java.preseed',
    mode   => '0600',
    backup => false,
  }

  package { "oracle-java7-installer":
    responsefile => '/tmp/java.preseed',
    require      => [
      File['/tmp/java.preseed'],
      Apt::Ppa['ppa:webupd8team/java']
    ]
  }
}
