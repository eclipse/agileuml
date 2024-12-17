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


print(MatrixLib.matrixMultiplication([[1,2], [3,4]], [[5,6], [7,8]]))
