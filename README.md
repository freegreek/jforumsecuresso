jforumsecuresso
===============

Our slightly patched version of jforumsecuresso that works well with
jforum 2.3.4.

How to build:

  * Download jforum v2.3.4 and unzip the WAR
  * Go to WEB-INF/lib and run
       mvn install:install-file -Dpackaging=jar -DartifactId=jforum \
       -Dversion=2.3.4 -Dfile=jforum-2.3.4.jar -DgroupId=net.jforum -DgeneratePom=true
  * Then go to jforumsecuresso dir and run
       mvn install
    The resulting jar should now be in target/
