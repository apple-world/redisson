Redis based Tomcat Session Manager
===

Stores session of [Apache Tomcat](http://tomcat.apache.org) in Redis and allows to distribute requests across a cluster of Tomcat servers. Implements non-sticky session management backed by Redis.  

Supports Apache Tomcat 6.x, 7.x, 8.x, 9.x

<sub>Please consider __[Redisson PRO](https://redisson.pro)__ version for advanced features and support by SLA.</sub>

Advantages
===

Current implementation differs from any other Redis based Tomcat Session Manager in terms of efficient storage and optimized writes. Each session attribute is written into Redis during each `HttpSession.setAttribute` invocation. While other solutions serialize whole session each time.

Usage
===

### 1. Add `RedissonSessionManager`

Add `RedissonSessionManager` into `tomcat/conf/context.xml`
   
   ```xml
<Manager className="org.redisson.tomcat.RedissonSessionManager"
	         configPath="${catalina.base}/redisson.conf" readMode="REDIS" updateMode="DEFAULT"/>
   ```
   `readMode` - read attributes mode. Two modes are available:
   * `MEMORY` - stores attributes into local Tomcat Session and Redis. Further Session updates propagated to local Tomcat Session using Redis-based events.
   * `REDIS` - stores attributes into Redis only.  Default mode.
   <br/>

   `updateMode` - attributes update mode. Two modes are available:
   * `DEFAULT` - session attributes are stored into Redis only through setAttribute method. Default mode.
   * `AFTER_REQUEST` - all session attributes are stored into Redis after each request.
   <br/>

   `configPath` - path to Redisson JSON or YAML config. See [configuration wiki page](https://github.com/redisson/redisson/wiki/2.-Configuration) for more details.

#### Shared Redisson instance

Each RedissonSessionManager created per Web Application and thus creates own Redisson instance. For multiple applications, using the same Redis setup, amount of Redisson instances could be reduced using JNDI registry:

1. Add shared redisson instance produced by `JndiRedissonFactory` into `tomcat/conf/server.xml` in `GlobalNamingResources` tag area:

```xml
  <GlobalNamingResources>
    <Resource name="bean/redisson" 
	      auth="Container"
              factory="org.redisson.JndiRedissonFactory"
              configPath="${catalina.base}/conf/redisson.yaml"
	      closeMethod="shutdown"/>
  </GlobalNamingResources>
```

2. Add `JndiRedissonSessionManager` with resource link to redisson instance into `tomcat/conf/context.xml`

```xml
    <ResourceLink name="bean/redisson"
                  global="bean/redisson"
		  type="org.redisson.api.RedissonClient" />

    <Manager className="org.redisson.tomcat.JndiRedissonSessionManager"
         readMode="REDIS"
         jndiName="bean/redisson" />
```

**2** Copy two jars into `TOMCAT_BASE/lib` directory:
  
1. __For JDK 1.8+__  
      [redisson-all-3.10.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-all&v=3.10.1&e=jar)
  
      for Tomcat 6.x  
      [redisson-tomcat-6-3.10.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-tomcat-6&v=3.10.1&e=jar)  
      for Tomcat 7.x  
      [redisson-tomcat-7-3.10.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-tomcat-7&v=3.10.1&e=jar)  
      for Tomcat 8.x  
      [redisson-tomcat-8-3.10.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-tomcat-8&v=3.10.1&e=jar)  
      for Tomcat 9.x  
      [redisson-tomcat-9-3.10.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-tomcat-9&v=3.10.1&e=jar)  
  
2. __For JDK 1.6+__  
      [redisson-all-2.15.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-all&v=2.15.1&e=jar)
  
      for Tomcat 6.x  
      [redisson-tomcat-6-2.15.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-tomcat-6&v=2.15.1&e=jar)  
      for Tomcat 7.x  
      [redisson-tomcat-7-2.15.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-tomcat-7&v=2.15.1&e=jar)  
      for Tomcat 8.x  
      [redisson-tomcat-8-2.15.1.jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.redisson&a=redisson-tomcat-8&v=2.15.1&e=jar)  


redisson-tomcat 開発手順
===============================

### build
    # pwebのjbossが5系の為、tomcat6系を使用する
    cd redisson-tomcat/redisson-tomcat-6

    # gradleでbuild。成功するとbuild/libs配下にredisson-tomcat-6-2.15.2.jarが作られる
    gradle

### debug
#### tomcat6
    cd debug

    # redisを起動
    docker-compose up -d

    # tomcatにjarを設置し、起動
    cp ../redisson-tomcat/redisson-tomcat-6/build/libs/redisson-tomcat-6-2.15.2.jar local/apache-tomcat-6.0.16/lib/
    ./local/apache-tomcat-6.0.16/bin/startup.sh

    # ブラウザで確認
    http://localhost:8080/hello/

    # tomcat停止
    ./local/apache-tomcat-6.0.16/bin/shutdown.sh

#### jboss5
    cd debug

    # redisを起動
    docker-compose up -d

    # jbosssにjarを設置し、起動
    cp ../redisson-tomcat/redisson-tomcat-6/build/libs/redisson-tomcat-6-2.15.2.jar local/jboss-5.1.0.GA/common/lib/
    ./local/jboss-5.1.0.GA/bin/run.sh -c default -b 0.0.0.0

    # ブラウザで確認
    http://localhost:8080/hello/

    # jboss停止
    ./local/jboss-5.1.0.GA/bin/shutdown.sh