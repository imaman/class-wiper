package com.blogspot.javadots.classwiper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({DependecnyExtractor_Tests.class, TestClassDetector_Tests.class, Main_Tests.class, Graph_Tests.class})
public class All_Tests {

}
