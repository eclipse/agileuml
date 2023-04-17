
/******************************
* Copyright (c) 2003--2023 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/
/* Package: Model-based Testing */ 

public class TestParameters
{ public static int maxInteger = 1024;
  public static int maxint = 2147483647;
 
  public static int minInteger = -1025;
  public static int minint = -2147483648; 
 
  public static long maxlong = 9223372036854775807L; 
  public static long minlong = -9223372036854775808L; 

  public static int maxLong = 100000;
  public static int minLong = -999999;
  
  public static double maxFloat = 3125.0891; 
    // Double.MAX_VALUE

  public static double minFloat = -655.276; 
    // -Double.MAX_VALUE

  /* Settings for flaw/technical debt detection */ 

  public static int cloneSizeLimit = 10; 
  public static int cyclomaticComplexityLimit = 10; 
  public static int numberOfDataFeaturesLimit = 20; 
  public static int numberOfOperationsLimit = 15; 
  public static int numberOfParametersLimit = 10; 

  public static int operationSizeLimit = 100; 
  public static int operationSizeWarning = 50; 

  public static int classSizeLimit = 1000; 

  public static int efoLimit = 10; 
  public static int efiLimit = 10; 

}

