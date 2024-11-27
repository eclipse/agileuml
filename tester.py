import ocl
import math
import re
import copy

from mathlib import *
from oclfile import *
from ocltype import *
from ocldate import *
from oclprocess import *
from ocliterator import *
from ocldatasource import *
from enum import Enum

import app
import MutationTest




intTestValues = [0, -1, 1, -1025, 1024]
longTestValues = [0, -1, 1, -999999, 100000]
doubleTestValues = [0, -1, 1, 3125.0891, 0.0000000000000000000000001]
booleanTestValues = [False, True]
stringTestValues = ["", " abc_XZ ", "#�$* &~@'"]

MAXOBJECTS = 500



fromvb_count = len(app.FromVB.fromvb_instances)
fromvb_FindArray_counts = [0 for _x in range(0,100)]
fromvb_FindArray_totals = [0 for _y in range(0,100)]

for _ex in app.FromVB.fromvb_instances :
  MutationTest.MutationTest.FindArray_mutation_tests(_ex,fromvb_FindArray_counts, fromvb_FindArray_totals)
  print("")

for _idx in range(0,len(fromvb_FindArray_counts)) :
  if fromvb_FindArray_totals[_idx] > 0 :
    print("Test " + str(_idx) + " effectiveness: " + str(100.0*fromvb_FindArray_counts[_idx]/fromvb_FindArray_totals[_idx]) + "%")



