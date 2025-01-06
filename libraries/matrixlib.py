import numpy as np


class MatrixLib :

  def rowMult(s, m) :   
    result = []
    for i in range(0, len(s)) :
      sum = 0 
      for k in range(0, len(m)) : 
        sum = sum + s[k]*(m[k][i])
      result.append(sum)
    return result

  def matrixMultiplication(m1, m2) : 
     m1array = np.array(m1)
     m2array = np.array(m2)
     res = np.matmul(m1array, m2array)
     return res.tolist()

  def subRows(m: list, s: list[int]) -> list:
    result = []
    for integer in s:
      if 0 <= integer <= len(m) - 1: # OCL starts indexing at 1, Python starts indexing at 0
        result.append(m[integer])
    return result

  def subMatrix(m: list[list], rows: list[int], cols: list[int]) -> list:
    result = []
    for row in rows:
      if 0 <= row <= len(m) - 1: # OCL starts indexing at 1, Python starts indexing at 0
        result.append(subRows(m[row], cols))
    return result

  def matrixExcludingRowColumn(m: list, row: int, col: int) -> list:
    result = []

    # Original note-for-note adaption (as much as possible) of OCL code
    # for i in range(len(m)):
    #   if i != row:
    #     r: list = m[i]
    #     for i in range(len(r)):
    #       if j != col:
    #         subrow: list = r[j]
    #         res.append(subrow)
    
    # Better, more "pythonic" approach
    for r, i in enumerate(m):
      # not doing an "if i != row and j != col" check prevents us from checking every single column in every single row
      if i != row:
        for subrow, j in enumerate(r):
          if j != col:
            res.append(subrow)
      
    return res

print(MatrixLib.matrixMultiplication([[1,2], [3,4]], [[5,6], [7,8]]))
