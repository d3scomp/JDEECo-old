In the ApacheRiver folder contains Apache River distribution. Go to ApacheRiver\examples\hello and run startingApacheRiver.bat to start all the necessary services.
d3s.tuplespace.test is the eclipse project with the test application.
Runnable components are d3s.tuplespaces.components.ComponentA, d3s.tuplespaces.components.ComponentB and d3s.tuplespaces.runtime.EnsemblingRuntime.
For each of these component create a new run configuration containing the following jvm arguments:
-Djava.security.policy=${project_loc}\policy.all
-Djava.rmi.server.RMIClassLoaderSpi=net.jini.loader.pref.PreferredClassProvider