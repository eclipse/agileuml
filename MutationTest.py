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
from oclrandom import *
from stringlib import *
from enum import Enum

import app

class MutationTest :

  def FindArray_mutation_tests_0(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 0
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 0 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_1(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 0
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 1 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_2(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 0
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 2 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_3(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 0
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 3 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_4(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 0
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 4 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_5(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = -1
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 5 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_6(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = -1
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 6 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_7(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = -1
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 7 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_8(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = -1
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 8 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_9(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = -1
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 9 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_10(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 1
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 10 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_11(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 1
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 11 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_12(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 1
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 12 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_13(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 1
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 13 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_14(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 1
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 14 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_15(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 1024
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 15 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_16(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 1024
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 16 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_17(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 1024
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 17 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_18(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 1024
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 18 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_19(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = 1024
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 19 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_20(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = -1025
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 20 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_21(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = -1025
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 21 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_22(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = -1025
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 22 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_23(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = -1025
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 23 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_24(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 0
    Lower = -1025
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 24 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_25(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 0
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 25 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_26(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 0
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 26 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_27(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 0
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 27 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_28(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 0
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 28 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_29(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 0
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 29 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_30(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = -1
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 30 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_31(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = -1
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 31 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_32(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = -1
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 32 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_33(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = -1
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 33 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_34(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = -1
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 34 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_35(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 1
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 35 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_36(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 1
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 36 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_37(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 1
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 37 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_38(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 1
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 38 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_39(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 1
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 39 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_40(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 1024
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 40 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_41(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 1024
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 41 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_42(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 1024
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 42 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_43(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 1024
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 43 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_44(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = 1024
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 44 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_45(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = -1025
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 45 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_46(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = -1025
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 46 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_47(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = -1025
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 47 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_48(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = -1025
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 48 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_49(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = -1
    Lower = -1025
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 49 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_50(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 0
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 50 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_51(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 0
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 51 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_52(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 0
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 52 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_53(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 0
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 53 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_54(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 0
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 54 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_55(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = -1
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 55 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_56(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = -1
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 56 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_57(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = -1
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 57 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_58(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = -1
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 58 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_59(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = -1
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 59 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_60(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 1
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 60 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_61(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 1
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 61 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_62(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 1
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 62 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_63(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 1
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 63 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_64(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 1
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 64 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_65(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 1024
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 65 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_66(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 1024
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 66 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_67(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 1024
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 67 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_68(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 1024
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 68 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_69(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = 1024
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 69 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_70(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = -1025
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 70 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_71(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = -1025
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 71 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_72(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = -1025
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 72 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_73(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = -1025
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 73 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_74(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1
    Lower = -1025
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 74 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_75(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 0
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 75 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_76(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 0
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 76 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_77(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 0
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 77 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_78(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 0
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 78 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_79(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 0
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 79 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_80(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = -1
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 80 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_81(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = -1
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 81 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_82(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = -1
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 82 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_83(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = -1
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 83 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_84(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = -1
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 84 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_85(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 1
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 85 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_86(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 1
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 86 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_87(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 1
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 87 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_88(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 1
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 88 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_89(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 1
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 89 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_90(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 1024
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 90 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_91(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 1024
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 91 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_92(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 1024
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 92 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_93(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 1024
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 93 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_94(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = 1024
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 94 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_95(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = -1025
    Upper = 0

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 95 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_96(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = -1025
    Upper = -1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 96 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_97(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = -1025
    Upper = 1

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 97 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_98(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = -1025
    Upper = 1024

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 98 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests_99(_self, _counts, _totals) :
    pass
    Arr = []
    Elem = 1024
    Lower = -1025
    Upper = -1025

    try :
      FindArray_result = _self.FindArray(Arr,Elem,Lower,Upper)
      print("Test 99 of FindArray on " + str(_self) + " result = " + str(FindArray_result))
    except:
      print("Unable to execute FindArray")
      return



  def FindArray_mutation_tests(_self, _counts, _totals) :
    pass
    MutationTest.FindArray_mutation_tests_0(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_1(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_2(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_3(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_4(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_5(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_6(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_7(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_8(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_9(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_10(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_11(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_12(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_13(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_14(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_15(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_16(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_17(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_18(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_19(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_20(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_21(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_22(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_23(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_24(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_25(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_26(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_27(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_28(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_29(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_30(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_31(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_32(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_33(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_34(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_35(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_36(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_37(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_38(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_39(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_40(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_41(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_42(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_43(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_44(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_45(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_46(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_47(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_48(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_49(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_50(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_51(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_52(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_53(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_54(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_55(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_56(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_57(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_58(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_59(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_60(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_61(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_62(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_63(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_64(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_65(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_66(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_67(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_68(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_69(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_70(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_71(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_72(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_73(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_74(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_75(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_76(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_77(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_78(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_79(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_80(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_81(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_82(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_83(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_84(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_85(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_86(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_87(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_88(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_89(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_90(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_91(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_92(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_93(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_94(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_95(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_96(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_97(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_98(_self,_counts,_totals)

    MutationTest.FindArray_mutation_tests_99(_self,_counts,_totals)




