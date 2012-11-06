In order to build this project jforum needs to be available in the local M2 repo.

Download the JForum source distribution and build it.

There is no JAR builder in their ant file so create one that has all the JForum classes in it, then install it locally using:

mvn install:install-file -Dpackaging=jar -DartifactId=jforum -Dversion=2.1.8 -Dfile=jforum-2.1.8.jar -DgroupId=net.jforum -DgeneratePom=true

