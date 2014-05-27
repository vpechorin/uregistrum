Welcome to uRegistrum
==========

uRegistrum is one of the major building blocks required for creating a java micro services infrastructure. It is a registry that enables micro services to quickly find and use other micro services over the Internet using simple REST protocol.

Features
==========
- Platform Independent (java app)
- Store local and remote urls for each service, with optional login/passwords
- REST endpoints to add/remove/lookup endpoints
- Embedded webserver (Jetty)
- Embedded database engine (H2 Database Engine), can be configured with other popular relational databases
- Packaged as a single executable jar

How to build
==========

You require the following to build uRegistrum:

- Latest stable Oracle JDK 7
- Latest stable Apache Maven
 $ mvn package
     

How to use
==========
	
 $ java -jar ./target/uregistrum-0.1.jar

The default address is http://localhost:11400/api/endpoints

uRegistrum
==========

 was made by [Victor Pechorin], a sole developer from Auckland, New Zealand.
