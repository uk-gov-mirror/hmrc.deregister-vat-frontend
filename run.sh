#!/usr/bin/env bash
sbt 'run 9153 -Dlogger.resource=logback-test.xml -Dplay.http.router=testOnlyDoNotUseInAppConf.Routes'