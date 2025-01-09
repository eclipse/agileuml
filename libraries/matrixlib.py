import numpy as np
import ocl

class MatrixLib :

  def rowMult(s: list, m: list) -> list:   
    result = []
    for i in range(0, len(s)) :
      sum = 0 
      for k in range(0, len(m)) : 
        sum = sum + s[k]*(m[k][i])
      result.append(sum)
    return result

  def matrixMultiplication(m1: list, m2: list) -> list: 
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
  
  def column(m: list, i: int) -> list:
    return [m[i]] if isinstance(m[i], list) else m[i]

  def shape(x) -> list:
    result: list = [0]
    if isinstance(x, list):
      sq: list = list(x)
      result = [len(x)]
      if len(sq) > 0:
        result = ocl.union(result, MatrixLib.shape(sq[0]))
    return result

  def singleValueMatrix(sh: list, x) -> list:
    if len(sh) == 0:
      return []
    elif len(sh) == 1:
      return [x for i in range(1, sh[0], 1)]
    else:
      return [MatrixLib.singleValueMatrix(ocl.tail(sh), x) for i in range(1, sh[0], 1)]

  def fillMatrixForm(sq: list, sh: list) -> list:
    if len(sh) == 0: 
      return []
    elif len(sh) == 1:
      return [sq[i] for i in range(sh[0])]
    else:
      result = []
      prod: int = ocl.prd(ocl.tail(sh))
      for i in range(sh[0]):
        rowi: list = MatrixLib.fillMatrixForm(ocl.listSubrange(sq, prod * (i - 1), (prod * i) - 1), ocl.tail(sh))
        result.append(rowi)
      return result

  def identityMatrix(n: int) -> list:
    result: list = []
    for i in range(n):
      result.append([]) # Need to append lists instead of assigning via result[i][j]
      for j in range(n):
        if i == j:
          result[i].append(1.0)
        else:
          result[i].append(0.0)
    return result

  def flattenMatrix(m: list) -> list:
    if len(m) == 0:
      return []
    elif isinstance(m[0], list):
      sq: list = m[0]
      return [ocl.union(MatrixLib.flattenMatrix(sq), MatrixLib.flattenMatrix(ocl.tail(m)))]
    else:
      return m

  def sumMatrix(m: list) -> float:
    if len(m) == 0:
      return 0.0
    elif isinstance(m[0], list):
      sq: list = m[0]
      return MatrixLib.sumMatrix(sq) + MatrixLib.sumMatrix(ocl.tail(m))
    else:
      dmat: list = ocl.union([0.0], m)
      return sum(dmat) # Python has a built-in sum function ready to use on a list

  def prdMatrix(m: list) -> float:
    if len(m) == 0:
      return 0.0
    elif isinstance(m[0], list):
      sq: list = m[0]
      return MatrixLib.prdMatrix(sq) * MatrixLib.prdMatrix(ocl.tail(m))
    else:
      dmat: list = ocl.union([1.0], m)
      return np.prod(dmat) # Faster solution (mainly for very large lists), see https://stackoverflow.com/a/55297341

  def elementwiseApply(m: list, f: callable) -> list:
    if len(m) == 0:
      return []
    elif isinstance(m[0], list):
      return [MatrixLib.elementwiseApply(list(_r), f) for _r in m]
    return [f(float(z)) for z in m]

  def elementwiseMult(m: list, x: float) -> list:
    if len(m) == 0:
      return []
    elif isinstance(m[0], list):
      return [MatrixLib.elementwiseMult(list(_r), x) for _r in m]
    return [float(z) * x for z in m]

  def elementwiseAdd(m: list, x: float) -> list:
    if len(m) == 0:
      return []
    elif isinstance(m[0], list):
      return [MatrixLib.elementwiseAdd(list(_r), x) for _r in m]
    return [float(z) + x for z in m]

  def elementwiseDivide(m: list, x: float) -> list:
    if len(m) == 0:
      return []
    elif isinstance(m[0], list):
      return [MatrixLib.elementwiseDivide(list(_r), x) for _r in m]
    return [float(z) / x for z in m]

  def elementwiseLess(m: list, x: float) -> list:
    if len(m) == 0:
      return []
    elif isinstance(m[0], list):
      return [MatrixLib.elementwiseLess(list(_r), x) for _r in m]
    return [float(z) < x for z in m]

  def elementwiseGreater(m: list, x: float) -> list:
    if len(m) == 0:
      return []
    elif isinstance(m[0], list):
      return [MatrixLib.elementwiseGreater(list(_r), x) for _r in m]
    return [float(z) > x for z in m]

  def elementwiseEqual(m: list, x: float) -> list:
    if len(m) == 0:
      return []
    elif isinstance(m[0], list):
      return [MatrixLib.elementwiseEqual(list(_r), x) for _r in m]
    return [float(z) == x for z in m]

  def detaux(x1: float, x2: float, y1: float, y2: float) -> float:
    return x1*y2 - x2*y1

  def determinant2(m: list) -> float:
    if len(m) == 2 and len(m[0]) == 2:
      m1: list = list(m[0])
      m2: list = list(m[1])

      d11: float = float(m[0][1])
      d12: float = float(m[0][1])
      d21: float = float(m[1][0])
      d22: float = float(m[1][1])

      return MatrixLib.detaux(d11, d12, d21, d22)

  def determinant3(m: list[list]) -> float:
    if len(m) == 3 and len(m[0]) == 3:
      subm1: list = MatrixLib.subMatrix(m, [2, 3], [2, 3])
      subm2: list = MatrixLib.subMatrix(m, [2, 3], [1, 3])
      subm3: list = MatrixLib.subMatrix(m, [2, 3], [1, 2])

      m1: list = list(m[0])

      return (float(m1[0]) * MatrixLib.determinant2(subm1)) + \
             (float(m1[1]) * MatrixLib.determinant2(subm2)) + \
             (float(m1[2]) * MatrixLib.determinant2(subm3))

  def determinant(m: list) -> float:
    n = len(m)
    if n == 1:
      return float(m[0])
    elif n == 2:
      return MatrixLib.determinant2(m)
    elif n == 3:
      return MatrixLib.determinant3(m)
    else:
      result: float = 0.0
      row: list = list(m[0])
      factor: int = 1
      for i in range(n):
        submat: list = MatrixLib.matrixExcludingRowColumn(m, 1, i)
        det: float = MatrixLib.determinant(submat)
        rowi: float = float(row[i])
        result += factor * rowi * det
        factor = -factor
      return result

  def rowAddition(m1: list, m2: list) -> list:
    if isinstance(m1[0], list):
      return [MatrixLib.rowAddition(sq1, sq2) for sq1, sq2 in zip(m1, m2)]
    return [float(m1j) + float(m2j) for m1j, m2j in zip(m1, m2)]

  def matrixAddition(m1: list, m2: list) -> list:
    return [MatrixLib.rowAddition(sq1, sq2) for sq1, sq2 in zip(m1, m2)]

  def rowSubtraction(m1: list, m2: list) -> list:
    if isinstance(m1[0], list):
      return [MatrixLib.rowSubtraction(sq1, sq2) for sq1, sq2 in zip(m1, m2)]
    return [float(m1j) - float(m2j) for m1j, m2j in zip(m1, m2)]

  def matrixSubtraction(m1: list, m2: list) -> list:
    return [MatrixLib.rowSubtraction(sq1, sq2) for sq1, sq2 in zip(m1, m2)]

  def rowDotProduct(m1: list, m2: list) -> list:
    if isinstance(m1[0], list):
      return [MatrixLib.rowDotProduct(sq1, sq2) for sq1, sq2 in zip(m1, m2)]
    return [float(m1j) * float(m2j) for m1j, m2j in zip(m1, m2)]

  def dotProduct(m1: list, m2: list) -> list:
    return [MatrixLib.dotProduct(sq1, sq2) for sq1, sq2 in zip(m1, m2)]

  def rowDotDivision(m1: list, m2: list) -> list:
    if isinstance(m1[0], list):
      return [MatrixLib.rowDotDivision(m1i, m2i) for m1i, m2i in zip(m1, m2)]
    return [float(m1j) / float(m2j) for m1j, m2j in zip(m1, m2)]

  def dotDivision(m1: list, m2: list) -> list:
    return [MatrixLib.rowDotDivision(sq1, sq2) for sq1, sq2 in zip(m1, m2)]

  def rowLess(m1: list, m2: list) -> list:
    if isinstance(m1[0], list):
      return [MatrixLib.rowLess(m1i, m2i) for m1i, m2i in zip(m1, m2)]
    return [m1j < m2j for m1j, m2j in zip(m1, m2)]

  def matrixLess(m1: list, m2: list) -> list:
    return [MatrixLib.rowLess(m1i, m2i) for m1i, m2i in zip(m1, m2)]

  def rowGreater(m1: list, m2: list) -> list:
    if isinstance(m1[0], list):
      return [MatrixLib.rowGreater(m1i, m2i) for m1i, m2i in zip(m1, m2)]
    return [m1j > m2j for m1j, m2j in zip(m1, m2)]

  def matrixGreater(m1: list, m2: list) -> list:
    return [MatrixLib.rowGreater(r1, r2) for r1, r2 in zip(m1, m2)]

  def transpose(m: list) -> list:
    if not isinstance(m[0], list):
      return m
    return [MatrixLib.column(m, i) for i, _ in enumerate(m)]

print(MatrixLib.matrixMultiplication([[1,2], [3,4]], [[5,6], [7,8]]))
print(MatrixLib.identityMatrix(5))
