package com.telstra.codechallenge.first;

import com.intuit.karate.junit5.Karate;

public class TestFirstProblem {
	  @Karate.Test
	  Karate testMicroservice() {
	    return Karate.run("firtProblem").relativeTo(getClass());
	  }
}
