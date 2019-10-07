/******************************
* Copyright (c) 2003,2019 Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

public class GluedVisual
{ LineData line;
  int xdisp = 0;
  int ydisp = 0;

  public GluedVisual(LineData ld, int x, int y)
  { line = ld;
    xdisp = x;
    ydisp = y;
  }
}
