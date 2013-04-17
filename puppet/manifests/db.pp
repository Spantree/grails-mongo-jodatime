node 'db' {
  stage { 'bootstrap': before => Stage['first']}
  stage { 'first': before => Stage['main'] }
  stage { 'last': }
  Stage['main'] -> Stage['last']

  class { "aptupdate": stage => "bootstrap" }

  class { "mongodb":
    ulimit_nofile => 20000,
  }

  host { "db.dev.gmj.local":
    ensure => present,
    ip => $ipaddress
  }
}