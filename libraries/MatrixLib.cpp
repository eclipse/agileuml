#include <iostream>
#include <string>
#include <vector>
#include <typeinfo>
// #include "OCLC++.h"
#include "OCLC++11.h"

using std::string;
using std::vector;
using std::cout;
using std::endl;

template <typename T>
class MatrixLib {
    private:
        static bool elementOfListIsList(vector<T> m, int i) {
            return string(typeid(m[i]).name()).find("vector") != string::npos;
        }
        static bool firstElementOfListIsList(vector<T> m) {
            return elementOfListIsList(m, 0);
        }
        static bool elementIsList(T* x) {
            return string(typeid(*x).name()).find("vector") != string::npos;
        }
    public:
        MatrixLib() {};
        static vector<double> rowMult(vector<double> s, vector<vector<double>> m) {
            vector<double> result;
            for (size_t i = 0; i < s.size(); i++) {
                double sum = 0.0;
                for (size_t k = 0; k < m.size(); k++) {
                    sum += s[k] * m[k][i];
                }
                result.push_back(sum);
            }
            return result;
        };
        static vector<vector<double>> rowMultiplication(vector<vector<double>> m1, vector<vector<double>> m2) {
            vector<vector<double>> result;
            for (vector<double> row: m1) {
                result.push_back(rowMult(row, m2));
            }
            return result;
        };
        static vector<T> subRows(vector<T> m, vector<int> s) {
            vector<T> result;
            for (auto integer: s) {
                if ((integer >= 0) && (integer <= m.size() - 1)) {
                    result.push_back(m[integer]);
                }
            }
            return result;
        }
        static vector<T> subMatrix(vector<vector<T>> m, vector<int> rows, vector<int> cols) {
            vector<T> result;
            for (auto row: rows) {
                if ((row >= 0) && (row <= m.size() - 1)) {
                    result.push_back(subRows(m[row], cols));
                }
            }
            return result;
        }
        static vector<vector<T>> matrixExcludingRowColumn(vector<T> m, int row, int col) {
            vector<vector<T>> result;
            for (int i = 0; i < m.size(); i++) {
                if (i == row) {
                    vector<T> r = m[i];
                    for (int j = 0; j < r.size(); i++) {
                        if (j != col) {
                            vector<T> subrow = r[j];
                            result.push_back(subrow);
                        }
                    }
                }
            }
            return result;
        }
        static vector<T> column(vector<T> m, int i) {
            if (elementOfListIsList(m, i)) {
                return {m[i]};
            }
            return m[i];
        }
        static vector<int> shape(T* x) {
            vector<int> result = {0};
            if (elementIsList(x)) {
                vector<T> sq;
                sq.push_back(*x);
                result = {(int) sq.size()};
                if (sq.size() > 0) {
                    vector<int>* resultptr = &result;
                    result = UmlRsdsLib<int>::concatenate(resultptr, shape(*x[0]));
                }
            }
            return result;
        }
        static vector<T> singleValueMatrix(vector<T> sh, T* x) {
            if (sh.size() == 0) {
                return {};
            } else if (sh.size() == 1) {
                vector<T> result;
                T upperLimit = sh[0];
                for (int i = 0; i < upperLimit; i++) {
                    result.push_back(&x);
                }
                return result;
            } else {
                vector<T> result;
                T upperLimit = sh[0];
                for (int i = 0; i < upperLimit; i++) {
                    result.push_back(singleValueMatrix((vector<T>) sh.end(), x));
                }
                return result;
            }
        }
        static vector<T> fillMatrixForm(vector<T> sq, vector<T> sh) {
            if (sh.size() == 0) {
                return {};
            } else if (sh.size() == 1) {
                vector<T> result;
                T upperLimit = sh[0];
                for (int i = 0; i < upperLimit; i++) {
                    result.push_back(sh[i]);
                }
                return result;
            } else {
                vector<T> result;
                T upperLimit = sh[0];
                int prod = 0;
                for (auto x: sh.end()) {
                    prod *= x;
                }
                for (int i = 0; i < upperLimit; i++) {
                    int startIndex = prod * (i - 1);
                    int endIndex = prod * i - 1;
                    vector<T> vectorSubrange = {};
                    if (startIndex < 0) {
                        startIndex = (sq.size() - 1) + startIndex;
                    }
                    if (endIndex < 0) {
                        endIndex = (sq.size() - 1) + endIndex;
                    }
                    for (int k = startIndex - 1; k < endIndex; k++) {
                        vectorSubrange.push_back(sq[k]);
                    }
                    vector<T> rowi = fillMatrixForm(vectorSubrange, (vector<T>) sh.end());
                    result.push_back(rowi);
                }
                return result;
            }
        }
        static vector<vector<double>> identityMatrix(int n) {
            vector<vector<double>> result;
            for (int i = 0; i < n; i++) {
                vector<double> interimList;
                for (int j = 0; j < n; j++) {
                    if (i == j) {
                        interimList.push_back(1.0);
                    } else {
                        interimList.push_back(0.0);
                    }
                }
                result.push_back(interimList);
            }
            return result;
        }
        static vector<T> flattenMatrix(vector<T> m) {
            if (m.size() == 0) {
                return {};
            }
            if (firstElementOfListIsList(m)) {
                vector<T> sq = m[0];
                return UmlRsdsLib<T>::concatenate(flattenMatrix(sq), &UmlRsdsLib<T>::tail(m));
            }
            else {
                return m;
            }
        }
        static double sumMatrix(vector<T> m) {
            if (m.size() == 0) {
                return 0.0;
            }

            if (firstElementOfListIsList(m)) {
                vector<T> sq = m[0];
                return sumMatrix(sq) + sumMatrix(UmlRsdsLib<T>::tail(m));
            }

            vector<double> dmat = UmlRsdsLib<T>::concatenate({0.0}, m);
            return UmlRsdsLib<double>::sum(&dmat);
        }
        static double prdMatrix(vector<T> m) {
            if (m.size() == 0) {
                return 1.0;
            }

            if (firstElementOfListIsList(m)) {
                vector<T> sq = m[0];
                return prdMatrix(sq) * prdMatrix(UmlRsdsLib<T>::tail(m));
            }

            vector<double> dmat = UmlRsdsLib<T>::concatenate({1.0}, m);
            return UmlRsdsLib<double>::sum(&dmat);
        }
        static vector<T> elementwiseApply(vector<T> m, T (*f)(double)) {
            if (m.size() == 0) {
                return {};
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(elementwiseApply({_r}, f));
                }
                return n;
            }

            vector<double> dmat(m.size());

            for (auto x: m) {
                double y = (double) x;
                dmat.push_back(&f(y));
            }
            return dmat;
        }
        static vector<T> selectElements(vector<T> m, T (*f)(double)) {
            if (m.size() == 0) {
                return {};
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(selectElements({_r}, f));
                }
                return n;
            }

            vector<double> dmat(m.size());

            for (auto x: m) {
                double y = (double) x;
                if (&f(y)) {
                    dmat.push_back(y);
                }
            }
            return dmat;
        }
        static vector<T> elementwiseNot(vector<T> m) {
            if (m.size() == 0) {
                return 0;
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(elementwiseNot({_r}));
                }
                return n;
            }

            vector<bool> dmat(m.size());

            for (auto z: m) {
                bool y = (bool) z;
                dmat.push_back(!y);
            }
            return dmat;
        }
        static vector<T> elementwiseMult(vector<T> m, double x) {
            if (m.size() == 0) {
                return {};
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(elementwiseMult({_r}, x));
                }
                return n;
            }

            vector<double> dmat(m.size());

            for (auto z: m) {
                double y = (double) z;
                dmat.push_back(y * x);
            }
            return dmat;
        }
        static vector<T> elementwisePower(vector<T> m, double x) {
            if (m.size() == 0) {
                return {};
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(elementwisePower({_r}, x));
                }
                return n;
            }

            vector<double> dmat(m.size());

            for (auto z: m) {
                double y = (double) z;
                dmat.push_back(y * x);
            }
            return dmat;
        }
        static vector<T> elementwiseAdd(vector<T> m, double x) {
            if (m.size() == 0) {
                return {};
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(elementwiseAdd({_r}, x));
                }
                return n;
            }

            vector<double> dmat(m.size());

            for (auto z: m) {
                double y = (double) z;
                dmat.push_back(y + x);
            }
            return dmat;
        }
        static vector<T> elementwiseSubtract(vector<T> m, double x) {
            if (m.size() == 0) {
                return {};
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(elementwiseSubtract({_r}, x));
                }
                return n;
            }

            vector<double> dmat(m.size());

            for (auto z: m) {
                double y = (double) z;
                dmat.push_back(y - x);
            }
            return dmat;
        }
        static vector<T> elementwiseDivide(vector<T> m, double x) {
            if (m.size() == 0) {
                return {};
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(elementwiseDivide({_r}, x));
                }
                return n;
            }

            vector<double> dmat(m.size());

            for (auto z: m) {
                double y = (double) z;
                dmat.push_back(y / x);
            }
            return dmat;
        }
        static vector<T> elementwiseLess(vector<T> m, double x) {
            if (m.size() == 0) {
                return 0;
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(elementwiseLess({_r}, x));
                }
                return n;
            }

            vector<bool> dmat(m.size());

            for (auto z: m) {
                bool y = (bool) z;
                dmat.push_back(y < x);
            }
            return dmat;
        }
        static vector<T> elementwiseGreater(vector<T> m, double x) {
            if (m.size() == 0) {
                return 0;
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(elementwiseGreater({_r}, x));
                }
                return n;
            }

            vector<bool> dmat(m.size());

            for (auto z: m) {
                bool y = (bool) z;
                dmat.push_back(y > x);
            }
            return dmat;
        }
        static vector<T> elementwiseEqual(vector<T> m, double x) {
            if (m.size() == 0) {
                return 0;
            }

            if (firstElementOfListIsList(m)) {
                vector<T> n(m.size());
                for (auto _r : m) {
                    n.push_back(elementwiseEqual({_r}, x));
                }
                return n;
            }

            vector<bool> dmat(m.size());

            for (auto z: m) {
                bool y = (bool) z;
                dmat.push_back(y == x);
            }
            return dmat;
        }
        static double detaux(double x1, double x2, double y1, double y2) {
            return x1 * y2 - x2 * y1;
        }
        static double determinant2(vector<T> m) {
            if (m.size() == 2 and m[0].size() == 2) {
                vector<T> m1 = (vector<T>) m[0];
                vector<T> m2 = (vector<T>) m[1];

                double d11 = (double) m[0][0];
                double d12 = (double) m[0][1];
                double d21 = (double) m[1][0];
                double d22 = (double) m[1][1];

                return detaux(d11, d12, d21, d22);
            }
            return 0.0;
        }
        static double determinant3(vector<T> m) {
            if (m.size() == 3 and m[0].size() == 3) {
                vector<vector<T>> subm1 = subMatrix(m, {2, 3}, {2, 3});
                vector<vector<T>> subm2 = subMatrix(m, {2, 3}, {1, 3});
                vector<vector<T>> subm3 = subMatrix(m, {2, 3}, {1, 2});

                vector<T> m1 = (vector<T>) m[0];

                return ((double) m1[0] * determinant2(subm1)) -
                ((double) m1[1] * determinant2(subm2)) +
                ((double) m1[2] * determinant2(subm3));
            }
            return 0.0;
        }
        static double determinant(vector<T> m) {
            int n = m.size();

            switch (n) {
                case 1:
                    return (double) m[0];
                case 2:
                    return determinant2(m);
                case 3:
                    return determinant3(m);
                default:
                    double res = 0.0;
                    vector<T> row = (vector<T>) m[0];
                    int factor = 1;

                    for (int i = 0; i < n; i++) {
                        vector<T> submat = matrixExcludingRowColumn(m, 0, i);
                        double det = determinant(submat);
                        double rowi = (double) row[i];
                        res += factor * rowi * det;
                        factor = -factor;
                    }

                    return res;
            }
        }
        static vector<T> rowAddition(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            if (firstElementOfListIsList(m1)) {
                for (int i = 0; i < m1.size(); i++) {
                    vector<T> sq1 = (vector<T>) m1[i];
                    vector<T> sq2 = (vector<T>) m2[i];
                    res.push_back(rowAddition(sq1, sq2));
                }
                return res;
            }

            for (int j = 0; j < m1.size(); j++) {
                double m1j = (double) m1[j];
                double m2j = (double) m2[j];
                res.push_back(m1j + m2j);
            }
            return res;
        }
        static vector<T> matrixAddition(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            for (int i = 0; i < m1.size(); i++) {
                vector<T> sq1 = (vector<T>) m1[i];
                vector<T> sq2 = (vector<T>) m2[i];
                res.push_back(rowAddition(sq1, sq2));
            }

            return res;
        }
        static vector<T> rowSubtraction(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            if (firstElementOfListIsList(m1)) {
                for (int i = 0; i < m1.size(); i++) {
                    vector<T> sq1 = (vector<T>) m1[i];
                    vector<T> sq2 = (vector<T>) m2[i];
                    res.push_back(rowSubtraction(sq1, sq2));
                }
                return res;
            }

            for (int j = 0; j < m1.size(); j++) {
                double m1j = (double) m1[j];
                double m2j = (double) m2[j];
                res.push_back(m1j - m2j);
            }
            return res;
        }
        static vector<T> matrixSubtraction(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            for (int i = 0; i < m1.size(); i++) {
                vector<T> sq1 = (vector<T>) m1[i];
                vector<T> sq2 = (vector<T>) m2[i];
                res.push_back(rowSubtraction(sq1, sq2));
            }

            return res;
        }
        static vector<T> rowDotProduct(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            if (firstElementOfListIsList(m1)) {
                for (int i = 0; i < m1.size(); i++) {
                    vector<T> sq1 = (vector<T>) m1[i];
                    vector<T> sq2 = (vector<T>) m2[i];
                    res.push_back(rowDotProduct(sq1, sq2));
                }
                return res;
            }

            for (int j = 0; j < m1.size(); j++) {
                double m1j = (double) m1[j];
                double m2j = (double) m2[j];
                res.push_back(m1j * m2j);
            }
            return res;
        }
        static vector<T> dotProduct(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            for (int i = 0; i < m1.size(); i++) {
                vector<T> sq1 = (vector<T>) m1[i];
                vector<T> sq2 = (vector<T>) m2[i];
                res.push_back(rowDotProduct(sq1, sq2));
            }

            return res;
        }
        static vector<T> rowDotDivision(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            if (firstElementOfListIsList(m1)) {
                for (int i = 0; i < m1.size(); i++) {
                    vector<T> sq1 = (vector<T>) m1[i];
                    vector<T> sq2 = (vector<T>) m2[i];
                    res.push_back(rowDotDivision(sq1, sq2));
                }
                return res;
            }

            for (int j = 0; j < m1.size(); j++) {
                double m1j = (double) m1[j];
                double m2j = (double) m2[j];
                res.push_back(m1j / m2j);
            }
            return res;
        }
        static vector<T> dotDivision(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            for (int i = 0; i < m1.size(); i++) {
                vector<T> sq1 = (vector<T>) m1[i];
                vector<T> sq2 = (vector<T>) m2[i];
                res.push_back(rowDotDivision(sq1, sq2));
            }

            return res;
        }
        static vector<T> rowLess(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            if (firstElementOfListIsList(m1)) {
                for (int i = 0; i < m1.size(); i++) {
                    vector<T> sq1 = (vector<T>) m1[i];
                    vector<T> sq2 = (vector<T>) m2[i];
                    res.push_back(rowLess(sq1, sq2));
                }
                return res;
            }

            for (int j = 0; j < m1.size(); j++) {
                double m1j = (double) m1[j];
                double m2j = (double) m2[j];
                res.push_back(m1j < m2j);
            }
            return res;
        }
        static vector<T> matrixLess(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            for (int i = 0; i < m1.size(); i++) {
                vector<T> sq1 = (vector<T>) m1[i];
                vector<T> sq2 = (vector<T>) m2[i];
                res.push_back(rowLess(sq1, sq2));
            }

            return res;
        }
        static vector<T> rowGreater(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            if (firstElementOfListIsList(m1)) {
                for (int i = 0; i < m1.size(); i++) {
                    vector<T> sq1 = (vector<T>) m1[i];
                    vector<T> sq2 = (vector<T>) m2[i];
                    res.push_back(rowGreater(sq1, sq2));
                }
                return res;
            }

            for (int j = 0; j < m1.size(); j++) {
                double m1j = (double) m1[j];
                double m2j = (double) m2[j];
                res.push_back(m1j > m2j);
            }
            return res;
        }
        static vector<T> matrixGreater(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            for (int i = 0; i < m1.size(); i++) {
                vector<T> sq1 = (vector<T>) m1[i];
                vector<T> sq2 = (vector<T>) m2[i];
                res.push_back(rowGreater(sq1, sq2));
            }

            return res;
        }
        static vector<T> rowMaximum(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            if (firstElementOfListIsList(m1)) {
                for (int i = 0; i < m1.size(); i++) {
                    vector<T> sq1 = (vector<T>) m1[i];
                    vector<T> sq2 = (vector<T>) m2[i];
                    res.push_back(rowMaximum(sq1, sq2));
                }
                return res;
            }

            for (int j = 0; j < m1.size(); j++) {
                double m1j = (double) m1[j];
                double m2j = (double) m2[j];
                if (m1j > m2j) {
                    res.push_back(m1j);
                } else {
                    res.push_back(m2j);
                }
            }
            return res;
        }
        static vector<T> matrixMaximum(vector<T> m1, vector<T> m2) {
            vector<T> res = {};

            for (int i = 0; i < m1.size(); i++) {
                vector<T> sq1 = (vector<T>) m1[i];
                vector<T> sq2 = (vector<T>) m2[i];
                res.push_back(rowMaximum(sq1, sq2));
            }

            return res;
        }
        static vector<T> transpose(vector<T> m) {
            if (firstElementOfListIsList(m)) {
                vector<T> res = {};
                for (int i = 0; i < m.size(); i++) {
                    res.push_back(column(m, i));
                }
                return res;
            } else {
                return m;
            }
        }
};

int main() {
    // MatrixLib<int>::column({1, 2, 3, 4}, 0);
    // MatrixLib<vector<int>>::column({{1, 2}, {3, 4}}, 0);
    vector<vector<double>> list = MatrixLib<void>::rowMultiplication({{1.0, 2.0}, {3.0, 4.0}}, {{5.0, 6.0}, {7.0, 8.0}});
    // vector<double> param = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0,8.0};
    // vector<double>* param_pointer = &param;
    // vector<int> list = MatrixLib<vector<double>>::shape(param_pointer);
    cout << list.size() << endl;
    for (auto value : list) {
        // cout << value << endl;
        for (auto innervalue : value) {
            cout << innervalue << ", ";
        }
        cout << endl;
    }
    cout << "end" << endl;
    return 0;
}