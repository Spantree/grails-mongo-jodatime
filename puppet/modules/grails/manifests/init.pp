class grails(
  $version = "2.2.1",
  $grails_target_path = "/usr/share/grails",
  $timeout  = 120
) {
  $zip_url = "http://dist.springframework.org.s3.amazonaws.com/release/GRAILS/grails-${version}.zip"

  # file { "/usr/share/grails":
  #   ensure => directory
  # }

  archive { "grails-${version}":
    ensure     => present,
    url        => $zip_url,
    checksum   => false,
    src_target => '/tmp',
    target     => $grails_target_path,
    extension  => 'zip',
    timeout    => $timeout,
  }

  file { "/usr/share/grails/default":
    ensure => link,
    target => "/usr/share/grails/grails-${version}",
    require => Archive["grails-${version}"]
  }

  file { "/etc/profile.d/set_grails_home.sh":
    ensure => file,
    group => root,
    owner => root,
    mode => 744,
    source => "puppet:///modules/grails/set_grails_home.sh",
    require => [
      Archive["grails-${version}"],
      File["/usr/share/grails/default"]
    ]
  }
}