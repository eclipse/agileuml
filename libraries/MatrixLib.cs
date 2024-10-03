
    public class MatrixLib
    {

        public MatrixLib()
        {

        }



        public override string ToString()
        {
            string _res_ = "(MatrixLib) ";
            return _res_;
        }

        public static ArrayList rowMult(ArrayList s, ArrayList m)
        {
            ArrayList result = new ArrayList();

            result = MatrixLib_Aux.collect_1(SystemTypes.integerSubrange(1, s.Count), m, s);
            return result;
        }


        public static ArrayList matrixMultiplication(ArrayList m1, ArrayList m2)
        {
            ArrayList result = new ArrayList();

            result = MatrixLib_Aux.collect_2(m1, m2);
            return result;
        }


        public static ArrayList subRows(ArrayList m, ArrayList s)
        {
            ArrayList result = new ArrayList();

            result = MatrixLib_Aux.collect_4(MatrixLib_Aux.select_3(s, m), m);
            return result;
        }


        public static ArrayList subMatrix(ArrayList m, ArrayList rows, ArrayList cols)
        {
            ArrayList result = new ArrayList();

            result = MatrixLib_Aux.collect_6(MatrixLib_Aux.select_5(rows, m), m, cols);
            return result;
        }


        public static ArrayList matrixExcludingRowColumn(ArrayList m, int row, int col)
        {
            ArrayList res = (new ArrayList());

            ArrayList _range2 = SystemTypes.integerSubrange(1, (m).Count);
            for (int _i1 = 0; _i1 < _range2.Count; _i1++)
            {
                int i = (int)_range2[_i1];
                if (i != row)
                {
                    ArrayList r = ((ArrayList)(m)[i - 1]);

                    ArrayList subrow = MatrixLib_Aux.collect_8(MatrixLib_Aux.select_7(SystemTypes.integerSubrange(1, (r).Count), col), r);

                    res = SystemTypes.append(res, subrow);

                }

            }
            return res;


        }


        public static ArrayList column(ArrayList m, int i)
        {
            ArrayList result = new ArrayList();

            if (((m)[1 - 1] is ArrayList))
            {
                result = MatrixLib_Aux.collect_9(m, i);
            }
            else
            {
                result = (ArrayList)((m)[i - 1]);
            }
            return result;
        }


        public static ArrayList shape(object x)
        {
            ArrayList res = MatrixLib_Aux.collect_10((new ArrayList()));

            if ((x is ArrayList))
            {
                ArrayList sq = ((ArrayList)x);

                res = SystemTypes.addSet((new ArrayList()), ((int)(sq).Count));
                if ((sq).Count > 0)
                { res = SystemTypes.concatenate(res, MatrixLib.shape((sq)[1 - 1])); }
                else { return res; }


            }
            else { return res; }

            return res;


        }


        public static ArrayList singleValueMatrix(ArrayList sh, object x)
        {
            if ((sh).Count == 0)
            { return (new ArrayList()); }
            else { { } /* No update form for: skip */ }

            if ((sh).Count == 1)
            { return MatrixLib_Aux.collect_11(SystemTypes.integerSubrange(1, ((int)sh[1 - 1])), x); }
            else { { } /* No update form for: skip */ }

            ArrayList res = (new ArrayList());

            res = MatrixLib_Aux.collect_12(SystemTypes.integerSubrange(1, ((int)sh[1 - 1])), sh, x);
            return res;




        }


        public static ArrayList fillMatrixFrom(ArrayList sq, ArrayList sh)
        {
            if ((sh).Count == 0)
            { return (new ArrayList()); }
            else { { } /* No update form for: skip */ }

            if ((sh).Count == 1)
            { return SystemTypes.subrange(sq, 1, ((int)sh[1 - 1])); }
            else { { } /* No update form for: skip */ }

            ArrayList res = (new ArrayList());

            int prod = SystemTypes.prdint(SystemTypes.tail(sh));

            ArrayList _range4 = SystemTypes.integerSubrange(1, ((int)sh[1 - 1]));
            for (int _i3 = 0; _i3 < _range4.Count; _i3++)
            {
                int i = (int)_range4[_i3];
                ArrayList rowi = MatrixLib.fillMatrixFrom(SystemTypes.subrange(sq, 1 + prod * (i - 1), prod * i), SystemTypes.tail(sh));

                res = SystemTypes.append(res, rowi);

            }
            return res;





        }


        public static ArrayList identityMatrix(int n)
        {
            ArrayList result = new ArrayList();

            result = MatrixLib_Aux.collect_14(SystemTypes.integerSubrange(1, n), n);
            return result;
        }


        public static ArrayList flattenMatrix(ArrayList m)
        {
            if ((m).Count == 0)
            { return (new ArrayList()); }
            else { { } /* No update form for: skip */ }

            if (((m)[1 - 1] is ArrayList))
            {
                ArrayList sq = ((ArrayList)(m)[1 - 1]);

                return SystemTypes.union(MatrixLib.flattenMatrix(sq), MatrixLib.flattenMatrix(SystemTypes.tail(m)));

            }
            else { { } /* No update form for: skip */ }

            return m;


        }


        public static double sumMatrix(ArrayList m)
        {
            if ((m).Count == 0)
            { return 0.0; }
            else { { } /* No update form for: skip */ }

            if (((m)[1 - 1] is ArrayList))
            {
                ArrayList sq = ((ArrayList)(m)[1 - 1]);

                return MatrixLib.sumMatrix(sq) + MatrixLib.sumMatrix(SystemTypes.tail(m));

            }
            else { { } /* No update form for: skip */ }

            ArrayList dmat = SystemTypes.concatenate(SystemTypes.addSet((new ArrayList()), ((double)0.0)), m);

            return SystemTypes.sumdouble(dmat);



        }


        public static double prdMatrix(ArrayList m)
        {
            if ((m).Count == 0)
            { return 1.0; }
            else { { } /* No update form for: skip */ }

            if (((m)[1 - 1] is ArrayList))
            {
                ArrayList sq = ((ArrayList)(m)[1 - 1]);

                return MatrixLib.prdMatrix(sq) * MatrixLib.prdMatrix(SystemTypes.tail(m));

            }
            else { { } /* No update form for: skip */ }

            ArrayList dmat = SystemTypes.concatenate(SystemTypes.addSet((new ArrayList()), ((double)1.0)), m);

            return SystemTypes.prddouble(dmat);

        }


        public static ArrayList elementwiseApply(ArrayList m, Func<double, double> f)
        {
            if ((m).Count == 0)
            { return (new ArrayList()); }
            else { { } /* No update form for: skip */ }

            if (((m)[1 - 1] is ArrayList))
            { return MatrixLib_Aux.collect_15(m, f); }
            else { { } /* No update form for: skip */ }

            ArrayList dmat = MatrixLib_Aux.collect_16((new ArrayList()));

            ArrayList _range7 = m;
            for (int _i6 = 0; _i6 < _range7.Count; _i6++)
            {
                object x = (object)_range7[_i6];
                double y = ((double)x);

                dmat = SystemTypes.append(dmat, f.Invoke(y));

            }
            return dmat;

        }


        public static ArrayList elementwiseMult(ArrayList m, double x)
        {
            if ((m).Count == 0)
            { return (new ArrayList()); }
            else { { } /* No update form for: skip */ }

            if (((m)[1 - 1] is ArrayList))
            { return MatrixLib_Aux.collect_17(m, x); }
            else { { } /* No update form for: skip */ }

            ArrayList dmat = MatrixLib_Aux.collect_16((new ArrayList()));

            ArrayList _range9 = m;
            for (int _i8 = 0; _i8 < _range9.Count; _i8++)
            {
                object z = (object)_range9[_i8];
                double y = ((double)z);

                dmat = SystemTypes.append(dmat, y * x);

            }
            return dmat;

        }


        public static ArrayList elementwiseAdd(ArrayList m, double x)
        {
            if ((m).Count == 0)
            { return (new ArrayList()); }
            else { { } /* No update form for: skip */ }

            if (((m)[1 - 1] is ArrayList))
            { return MatrixLib_Aux.collect_18(m, x); }
            else { { } /* No update form for: skip */ }

            ArrayList dmat = MatrixLib_Aux.collect_16((new ArrayList()));

            ArrayList _range11 = m;
            for (int _i10 = 0; _i10 < _range11.Count; _i10++)
            {
                object z = (object)_range11[_i10];
                double y = ((double)z);

                dmat = SystemTypes.append(dmat, y + x);

            }
            return dmat;

        }


        public static ArrayList elementwiseDivide(ArrayList m, double x)
        {
            if ((m).Count == 0)
            { return (new ArrayList()); }
            else { { } /* No update form for: skip */ }

            if (((m)[1 - 1] is ArrayList))
            { return MatrixLib_Aux.collect_19(m, x); }
            else { { } /* No update form for: skip */ }

            ArrayList dmat = MatrixLib_Aux.collect_16((new ArrayList()));

            ArrayList _range13 = m;
            for (int _i12 = 0; _i12 < _range13.Count; _i12++)
            {
                object z = (object)_range13[_i12];
                double y = ((double)z);

                dmat = SystemTypes.append(dmat, y / x);

            }
            return dmat;

        }


        public static ArrayList elementwiseLess(ArrayList m, double x)
        {
            if ((m).Count == 0)
            { return (new ArrayList()); }
            else { { } /* No update form for: skip */ }

            if (((m)[1 - 1] is ArrayList))
            { return MatrixLib_Aux.collect_20(m, x); }
            else { { } /* No update form for: skip */ }

            ArrayList dmat = MatrixLib_Aux.collect_21((new ArrayList()));

            ArrayList _range15 = m;
            for (int _i14 = 0; _i14 < _range15.Count; _i14++)
            {
                object z = (object)_range15[_i14];
                double y = ((double)z);

                dmat = SystemTypes.append(dmat, ((y < x) ? (true) : (false)));

            }
            return dmat;
        }


        public static ArrayList elementwiseGreater(ArrayList m, double x)
        {
            if ((m).Count == 0)
            { return (new ArrayList()); }
            else { { } /* No update form for: skip */ }

            if (((m)[1 - 1] is ArrayList))
            { return MatrixLib_Aux.collect_22(m, x); }
            else { { } /* No update form for: skip */ }

            ArrayList dmat = MatrixLib_Aux.collect_21((new ArrayList()));

            ArrayList _range17 = m;
            for (int _i16 = 0; _i16 < _range17.Count; _i16++)
            {
                object z = (object)_range17[_i16];
                double y = ((double)z);

                dmat = SystemTypes.append(dmat, ((y > x) ? (true) : (false)));

            }
            return dmat;

        }


        public static ArrayList elementwiseEqual(ArrayList m, double x)
        {
            if ((m).Count == 0)
            { return (new ArrayList()); }
            else { { } /* No update form for: skip */ }

            if (((m)[1 - 1] is ArrayList))
            { return MatrixLib_Aux.collect_23(m, x); }
            else { { } /* No update form for: skip */ }

            ArrayList dmat = MatrixLib_Aux.collect_21((new ArrayList()));

            ArrayList _range19 = m;
            for (int _i18 = 0; _i18 < _range19.Count; _i18++)
            {
                object z = (object)_range19[_i18];
                double y = ((double)z);

                dmat = SystemTypes.append(dmat, ((x == y) ? (true) : (false)));

            }
            return dmat;
        }


        public static double detaux(double x1, double x2, double y1, double y2)
        {
            double result = 0.0;

            result = x1 * y2 - x2 * y1;
            return result;
        }


        public static double determinant2(ArrayList m)
        { // m is an array of double arrays

            ArrayList m0 = (ArrayList) m[0];
            ArrayList m1 = (ArrayList) m[1];

            double result = 0.0;
            if ((m).Count != 2 || m0.Count != 2) { return result; }

            result = MatrixLib.detaux(((double) m0[0]), ((double)(m0)[2 - 1]), ((double)(m1)[1 - 1]), ((double)(m1)[2 - 1]));
            return result;
        }


        public static double determinant3(ArrayList m)
        {
            ArrayList m0 = (ArrayList)m[0];
         
            ArrayList subm1 = MatrixLib.subMatrix(m, SystemTypes.addSet(SystemTypes.addSet((new ArrayList()), ((int)2)), ((int)3)), SystemTypes.addSet(SystemTypes.addSet((new ArrayList()), ((int)2)), ((int)3)));

            ArrayList subm2 = MatrixLib.subMatrix(m, SystemTypes.addSet(SystemTypes.addSet((new ArrayList()), ((int)2)), ((int)3)), SystemTypes.addSet(SystemTypes.addSet((new ArrayList()), ((int)1)), ((int)3)));

            ArrayList subm3 = MatrixLib.subMatrix(m, SystemTypes.addSet(SystemTypes.addSet((new ArrayList()), ((int)2)), ((int)3)), SystemTypes.addSet(SystemTypes.addSet((new ArrayList()), ((int)1)), ((int)2)));

            return ((double)(m0)[0]) * MatrixLib.determinant2(subm1) - ((double)(m0)[1]) * MatrixLib.determinant2(subm2) + ((double)(m0)[2]) * MatrixLib.determinant2(subm3);

        }


        public static double determinant(ArrayList m)
        {
            int n = (m).Count;

            if (n == 1)
            { return ((double)(m)[1 - 1]); }
            else { { } /* No update form for: skip */ }

            if (n == 2)
            { return MatrixLib.determinant2(m); }
            else { { } /* No update form for: skip */ }

            if (n == 3)
            { return MatrixLib.determinant3(m); }
            else { { } /* No update form for: skip */ }

            double res = 0.0;

            ArrayList row = ((ArrayList)(m)[1 - 1]);

            int factor = 1;

            ArrayList _range21 = SystemTypes.integerSubrange(1, n);
            for (int _i20 = 0; _i20 < _range21.Count; _i20++)
            {
                int i = (int)_range21[_i20];
                ArrayList submat = MatrixLib.matrixExcludingRowColumn(m, 1, i);

                double det = MatrixLib.determinant(submat);

                double rowi = ((double)(row)[i - 1]);

                res = res + factor * rowi * det;
                factor = -factor;

            }
            return res;

        }


        public static ArrayList rowAddition(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            if (((m1)[1 - 1] is ArrayList))
            {
                ArrayList _range23 = SystemTypes.integerSubrange(1, (m1).Count);
                for (int _i22 = 0; _i22 < _range23.Count; _i22++)
                {
                    int i = (int)_range23[_i22];
                    ArrayList sq1 = ((ArrayList)(m1)[i - 1]);

                    ArrayList sq2 = ((ArrayList)(m2)[i - 1]);

                    res = SystemTypes.append(res, MatrixLib.rowAddition(sq1, sq2));


                }
                return res;
            }
            else { { } /* No update form for: skip */ }

            ArrayList _range25 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i24 = 0; _i24 < _range25.Count; _i24++)
            {
                int j = (int)_range25[_i24];
                double m1j = ((double)(m1)[j - 1]);

                double m2j = ((double)(m2)[j - 1]);

                res = SystemTypes.append(res, m1j + m2j);


            }
            return res;
        }


        public static ArrayList matrixAddition(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            ArrayList _range27 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i26 = 0; _i26 < _range27.Count; _i26++)
            {
                int i = (int)_range27[_i26];
                ArrayList sq1 = ((ArrayList)(m1)[i - 1]);

                ArrayList sq2 = ((ArrayList)(m2)[i - 1]);

                res = SystemTypes.append(res, MatrixLib.rowAddition(sq1, sq2));


            }
            return res;

        }


        public static ArrayList rowSubtraction(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            if (((m1)[1 - 1] is ArrayList))
            {
                ArrayList _range29 = SystemTypes.integerSubrange(1, (m1).Count);
                for (int _i28 = 0; _i28 < _range29.Count; _i28++)
                {
                    int i = (int)_range29[_i28];
                    ArrayList sq1 = ((ArrayList)(m1)[i - 1]);

                    ArrayList sq2 = ((ArrayList)(m2)[i - 1]);

                    res = SystemTypes.append(res, MatrixLib.rowSubtraction(sq1, sq2));


                }
                return res;
            }
            else { { } /* No update form for: skip */ }

            ArrayList _range31 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i30 = 0; _i30 < _range31.Count; _i30++)
            {
                int j = (int)_range31[_i30];
                double m1j = ((double)(m1)[j - 1]);

                double m2j = ((double)(m2)[j - 1]);

                res = SystemTypes.append(res, m1j - m2j);


            }
            return res;
        }


        public static ArrayList matrixSubtraction(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            ArrayList _range33 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i32 = 0; _i32 < _range33.Count; _i32++)
            {
                int i = (int)_range33[_i32];
                ArrayList sq1 = ((ArrayList)(m1)[i - 1]);

                ArrayList sq2 = ((ArrayList)(m2)[i - 1]);

                res = SystemTypes.append(res, MatrixLib.rowSubtraction(sq1, sq2));


            }
            return res;


        }


        public static ArrayList rowDotProduct(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            if (((m1)[0] is ArrayList))
            {
                ArrayList _range35 = SystemTypes.integerSubrange(1, (m1).Count);
                for (int _i34 = 0; _i34 < _range35.Count; _i34++)
                {
                    int i = (int)_range35[_i34];
                    ArrayList sq1 = ((ArrayList)(m1)[i - 1]);

                    ArrayList sq2 = ((ArrayList)(m2)[i - 1]);

                    res = SystemTypes.append(res, MatrixLib.rowDotProduct(sq1, sq2));


                }
                return res;
            }

            ArrayList _range37 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i36 = 0; _i36 < _range37.Count; _i36++)
            {
                int j = (int)_range37[_i36];
                double m1j = ((double)(m1)[j - 1]);

                double m2j = ((double)(m2)[j - 1]);

                res = SystemTypes.append(res, m1j * m2j);


            }
            return res;



        }


        public static ArrayList dotProduct(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            ArrayList _range39 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i38 = 0; _i38 < _range39.Count; _i38++)
            {
                int i = (int)_range39[_i38];
                ArrayList sq1 = ((ArrayList)(m1)[i - 1]);

                ArrayList sq2 = ((ArrayList)(m2)[i - 1]);

                res = SystemTypes.append(res, MatrixLib.rowDotProduct(sq1, sq2));


            }
            return res;


        }


        public static ArrayList rowDotDivision(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            if (((m1)[0] is ArrayList))
            {
                ArrayList _range41 = SystemTypes.integerSubrange(1, (m1).Count);
                for (int _i40 = 0; _i40 < _range41.Count; _i40++)
                {
                    int i = (int)_range41[_i40];
                    ArrayList m1i = ((ArrayList)(m1)[i - 1]);

                    ArrayList m2i = ((ArrayList)(m2)[i - 1]);

                    res = SystemTypes.append(res, MatrixLib.rowDotDivision(m1i, m2i));


                }
                return res;
            }

            ArrayList _range43 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i42 = 0; _i42 < _range43.Count; _i42++)
            {
                int j = (int)_range43[_i42];
                double m1j = ((double)(m1)[j - 1]);

                double m2j = ((double)(m2)[j - 1]);

                res = SystemTypes.append(res, m1j / m2j);


            }
            return res;



        }


        public static ArrayList dotDivision(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            ArrayList _range45 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i44 = 0; _i44 < _range45.Count; _i44++)
            {
                int i = (int)_range45[_i44];
                ArrayList sq1 = ((ArrayList)(m1)[i - 1]);

                ArrayList sq2 = ((ArrayList)(m2)[i - 1]);

                res = SystemTypes.append(res, MatrixLib.rowDotDivision(sq1, sq2));


            }
            return res;


        }


        public static ArrayList rowLess(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            if (((m1)[0] is ArrayList))
            {
                ArrayList _range47 = SystemTypes.integerSubrange(1, (m1).Count);
                for (int _i46 = 0; _i46 < _range47.Count; _i46++)
                {
                    int i = (int)_range47[_i46];
                    ArrayList m1i = ((ArrayList)(m1)[i - 1]);

                    ArrayList m2i = ((ArrayList)(m2)[i - 1]);

                    res = SystemTypes.append(res, MatrixLib.rowLess(m1i, m2i));


                }
                return res;
            }

            ArrayList _range49 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i48 = 0; _i48 < _range49.Count; _i48++)
            {
                int j = (int)_range49[_i48];
                double m1j = ((double)(m1)[j - 1]);

                double m2j = ((double)(m2)[j - 1]);

                res = SystemTypes.append(res, ((m1j < m2j) ? (true) : (false)));


            }
            return res;



        }


        public static ArrayList matrixLess(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            ArrayList _range51 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i50 = 0; _i50 < _range51.Count; _i50++)
            {
                int i = (int)_range51[_i50];
                ArrayList m1i = ((ArrayList)(m1)[i - 1]);

                ArrayList m2i = ((ArrayList)(m2)[i - 1]);

                res = SystemTypes.append(res, MatrixLib.rowLess(m1i, m2i));


            }
            return res;


        }


        public static ArrayList rowGreater(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            if (((m1)[0] is ArrayList))
            {
                ArrayList _range53 = SystemTypes.integerSubrange(1, (m1).Count);
                for (int _i52 = 0; _i52 < _range53.Count; _i52++)
                {
                    int i = (int)_range53[_i52];
                    ArrayList m1i = ((ArrayList)(m1)[i - 1]);

                    ArrayList m2i = ((ArrayList)(m2)[i - 1]);

                    res = SystemTypes.append(res, MatrixLib.rowGreater(m1i, m2i));


                }
                return res;
            }

            ArrayList _range55 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i54 = 0; _i54 < _range55.Count; _i54++)
            {
                int j = (int)_range55[_i54];
                double m1j = ((double)(m1)[j - 1]);

                double m2j = ((double)(m2)[j - 1]);

                res = SystemTypes.append(res, ((m1j > m2j) ? (true) : (false)));


            }
            return res;



        }


        public static ArrayList matrixGreater(ArrayList m1, ArrayList m2)
        {
            ArrayList res = (new ArrayList());

            ArrayList _range57 = SystemTypes.integerSubrange(1, (m1).Count);
            for (int _i56 = 0; _i56 < _range57.Count; _i56++)
            {
                int i = (int)_range57[_i56];
                ArrayList r1 = ((ArrayList)(m1)[i - 1]);

                ArrayList r2 = ((ArrayList)(m2)[i - 1]);

                res = SystemTypes.append(res, MatrixLib.rowGreater(r1, r2));


            }
            return res;


        }


        public static ArrayList transpose(ArrayList m)
        {
            if (((m)[0] is ArrayList))
            { { } /* No update form for: skip */ }
            else { return m; }

            ArrayList res = (new ArrayList());

            ArrayList _range59 = SystemTypes.integerSubrange(1, (m).Count);
            for (int _i58 = 0; _i58 < _range59.Count; _i58++)
            {
                int i = (int)_range59[_i58];
                res = SystemTypes.append(res, MatrixLib.column(m, i));
            }
            return res;



        }



    }


class MatrixLib_Aux
    {
        public static ArrayList select_3(ArrayList _l, ArrayList m)
        { // Implements: s->select(i | 1 <= i & i <= m->size())
            ArrayList _results_3 = new ArrayList();
            for (int _iselect = 0; _iselect < _l.Count; _iselect++)
            {
                int i = (int)_l[_iselect];
                if (1 <= i && i <= (m).Count)
                { _results_3.Add(i); }
            }
            return _results_3;
        }

        public static ArrayList select_5(ArrayList _l, ArrayList m)
        { // Implements: rows->select(i | 1 <= i & i <= m->size())
            ArrayList _results_5 = new ArrayList();
            for (int _iselect = 0; _iselect < _l.Count; _iselect++)
            {
                int i = (int)_l[_iselect];
                if (1 <= i && i <= (m).Count)
                { _results_5.Add(i); }
            }
            return _results_5;
        }

        public static ArrayList select_7(ArrayList _l, int col)
        { // Implements: Integer.subrange(1,r->size())->select(j | j /= col)
            ArrayList _results_7 = new ArrayList();
            for (int _iselect = 0; _iselect < _l.Count; _iselect++)
            {
                int j = (int)_l[_iselect];
                if (j != col)
                { _results_7.Add(j); }
            }
            return _results_7;
        }






        public static ArrayList collect_0(ArrayList _l, ArrayList s, ArrayList m, int i)
        { // Implements: Integer.subrange(1,m.size)->collect( k | s[k] * ( m[k]->at(i) ) )
            ArrayList _results_0 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int k = (int)_l[_icollect];
                ArrayList mk = (ArrayList)m[k - 1];

                _results_0.Add((double)s[k - 1] * (double)mk[i - 1]);
            }
            return _results_0;
        }

        public static ArrayList collect_1(ArrayList _l, ArrayList m, ArrayList s)
        { // Implements: Integer.subrange(1,s.size)->collect( i | Integer.Sum(1,m.size,k,s[k] * ( m[k]->at(i) )) )
            ArrayList _results_1 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int i = (int)_l[_icollect];
                _results_1.Add(SystemTypes.sumdouble(MatrixLib_Aux.collect_0(SystemTypes.integerSubrange(1, m.Count), s, m, i)));
            }
            return _results_1;
        }

        public static ArrayList collect_2(ArrayList _l, ArrayList m2)
        { // Implements: m1->collect( row | MatrixLib.rowMult(row,m2) )
            ArrayList _results_2 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                ArrayList row = (ArrayList)_l[_icollect];
                _results_2.Add(MatrixLib.rowMult(row, m2));
            }
            return _results_2;
        }

        public static ArrayList collect_4(ArrayList _l, ArrayList m)
        { // Implements: s->select( i | 1 <= i & i <= m->size() )->collect( j | m->at(j) )
            ArrayList _results_4 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int j = (int)_l[_icollect];
                _results_4.Add((m)[j - 1]);
            }
            return _results_4;
        }

        public static ArrayList collect_6(ArrayList _l, ArrayList m, ArrayList cols)
        { // Implements: rows->select( i | 1 <= i & i <= m->size() )->collect( j | MatrixLib.subRows(m->at(j),cols) )
            ArrayList _results_6 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int j = (int)_l[_icollect];
                _results_6.Add(MatrixLib.subRows(((ArrayList)m[j - 1]), cols));
            }
            return _results_6;
        }

        public static ArrayList collect_8(ArrayList _l, ArrayList r)
        { // Implements: Integer.subrange(1,r->size())->select( j | j /= col )->collect( j | r->at(j) )
            ArrayList _results_8 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int j = (int)_l[_icollect];
                _results_8.Add((r)[j - 1]);
            }
            return _results_8;
        }

        public static ArrayList collect_9(ArrayList _l, int i)
        { // Implements: m->collect( r | r->oclAsType(Sequence)->at(i) )
            ArrayList _results_9 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object r = (object)_l[_icollect];
                _results_9.Add((((ArrayList)r))[i - 1]);
            }
            return _results_9;
        }

        public static ArrayList collect_10(ArrayList _l)
        { // Implements: Sequence{}->collect( object_10_xx | 0 )
            ArrayList _results_10 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object object_10_xx = (object)_l[_icollect];
                _results_10.Add(0);
            }
            return _results_10;
        }

        public static ArrayList collect_11(ArrayList _l, object x)
        { // Implements: Integer.subrange(1,sh->at(1))->collect( int_11_xx | x )
            ArrayList _results_11 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int int_11_xx = (int)_l[_icollect];
                _results_11.Add(x);
            }
            return _results_11;
        }

        public static ArrayList collect_12(ArrayList _l, ArrayList sh, object x)
        { // Implements: Integer.subrange(1,sh->at(1))->collect( int_12_xx | MatrixLib.singleValueMatrix(sh->tail(),x) )
            ArrayList _results_12 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int int_12_xx = (int)_l[_icollect];
                _results_12.Add(MatrixLib.singleValueMatrix(SystemTypes.tail(sh), x));
            }
            return _results_12;
        }

        public static ArrayList collect_13(ArrayList _l, int i)
        { // Implements: Integer.subrange(1,n)->collect( j | if i = j then 1.0 else 0.0 endif )
            ArrayList _results_13 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int j = (int)_l[_icollect];
                _results_13.Add(((i == j) ? (1.0) : (0.0)));
            }
            return _results_13;
        }

        public static ArrayList collect_14(ArrayList _l, int n)
        { // Implements: Integer.subrange(1,n)->collect( i | Integer.subrange(1,n)->collect( j | if i = j then 1.0 else 0.0 endif ) )
            ArrayList _results_14 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                int i = (int)_l[_icollect];
                _results_14.Add(MatrixLib_Aux.collect_13(SystemTypes.integerSubrange(1, n), i));
            }
            return _results_14;
        }

        public static ArrayList collect_15(ArrayList _l, Func<double, double> f)
        { // Implements: m->collect( _r | MatrixLib.elementwiseApply(_r->oclAsType(Sequence),f) )
            ArrayList _results_15 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object _r = (object)_l[_icollect];
                _results_15.Add(MatrixLib.elementwiseApply(((ArrayList)_r), f));
            }
            return _results_15;
        }

        public static ArrayList collect_16(ArrayList _l)
        { // Implements: Sequence{}->collect( object_16_xx | 0.0 )
            ArrayList _results_16 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object object_16_xx = (object)_l[_icollect];
                _results_16.Add(0.0);
            }
            return _results_16;
        }

        public static ArrayList collect_17(ArrayList _l, double x)
        { // Implements: m->collect( _r | MatrixLib.elementwiseMult(_r->oclAsType(Sequence),x) )
            ArrayList _results_17 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object _r = (object)_l[_icollect];
                _results_17.Add(MatrixLib.elementwiseMult(((ArrayList)_r), x));
            }
            return _results_17;
        }

        public static ArrayList collect_18(ArrayList _l, double x)
        { // Implements: m->collect( _r | MatrixLib.elementwiseAdd(_r->oclAsType(Sequence),x) )
            ArrayList _results_18 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object _r = (object)_l[_icollect];
                _results_18.Add(MatrixLib.elementwiseAdd(((ArrayList)_r), x));
            }
            return _results_18;
        }

        public static ArrayList collect_19(ArrayList _l, double x)
        { // Implements: m->collect( _r | MatrixLib.elementwiseDivide(_r->oclAsType(Sequence),x) )
            ArrayList _results_19 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object _r = (object)_l[_icollect];
                _results_19.Add(MatrixLib.elementwiseDivide(((ArrayList)_r), x));
            }
            return _results_19;
        }

        public static ArrayList collect_20(ArrayList _l, double x)
        { // Implements: m->collect( _r | MatrixLib.elementwiseLess(_r->oclAsType(Sequence),x) )
            ArrayList _results_20 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object _r = (object)_l[_icollect];
                _results_20.Add(MatrixLib.elementwiseLess(((ArrayList)_r), x));
            }
            return _results_20;
        }

        public static ArrayList collect_21(ArrayList _l)
        { // Implements: Sequence{}->collect( object_21_xx | false )
            ArrayList _results_21 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object object_21_xx = (object)_l[_icollect];
                _results_21.Add(false);
            }
            return _results_21;
        }

        public static ArrayList collect_22(ArrayList _l, double x)
        { // Implements: m->collect( _r | MatrixLib.elementwiseGreater(_r->oclAsType(Sequence),x) )
            ArrayList _results_22 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object _r = (object)_l[_icollect];
                _results_22.Add(MatrixLib.elementwiseGreater(((ArrayList)_r), x));
            }
            return _results_22;
        }

        public static ArrayList collect_23(ArrayList _l, double x)
        { // Implements: m->collect( _r | MatrixLib.elementwiseEqual(_r->oclAsType(Sequence),x) )
            ArrayList _results_23 = new ArrayList();
            for (int _icollect = 0; _icollect < _l.Count; _icollect++)
            {
                object _r = (object)_l[_icollect];
                _results_23.Add(MatrixLib.elementwiseEqual(((ArrayList)_r), x));
            }
            return _results_23;
        }

    }
