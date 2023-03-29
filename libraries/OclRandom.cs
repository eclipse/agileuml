
    class OclRandom
    {
        private int ix; // internal
        private int iy; // internal
        private int iz; // internal

        public OclRandom()
        {
            ix = 0;
            iy = 0;
            iz = 0;

        }

        public void setix(int ixval)
        { ix = ixval; }

        public void setiy(int iyval)
        { iy = iyval; }

        public int getiz()
        { return iz; }

        public int getix()
        { return ix; }

        public int getiy()
        { return iy; }

        public void setiz(int izval)
        { iz = izval; }

        public static OclRandom newOclRandom()
        {
            OclRandom result = null;

            OclRandom rd = new OclRandom();
            rd.setix(1001);
            rd.setiy(781);
            rd.setiz(913);
            result = rd;
            return result;
        }


        public static OclRandom newOclRandom(long n)
        {
            OclRandom result = null;

            OclRandom rd = new OclRandom();
            rd.setix((int)n % 30269);
            rd.setiy((int)(n % 30307));
            rd.setiz((int)(n % 30323));
            result = rd;
            return result;
        }


        public void setSeeds(int x, int y, int z)
        {
            setix(x);
            setiy(y);
            setiz(z);
        }

        public void setSeed(long n)
        {
            setix((int)(n % 30269));
            setiy((int)(n % 30307));
            setiz((int)(n % 30323));
        }

        public double nrandom()
        {
            OclRandom oclrandomx = this;
            setix((this.getix() * 171) % 30269);
            setiy((this.getiy() * 172) % 30307);
            setiz((this.getiz() * 170) % 30323);
            return (this.getix() / 30269.0 + this.getiy() / 30307.0 + this.getiz() / 30323.0);
        }


        public double nextDouble()
        {
            double result = 0.0;
            double r = this.nrandom();
            result = (r - ((int)Math.Floor(r)));
            return result;
        }


        public double nextFloat()
        {
            double result = 0.0;

            double r = this.nrandom();
            result = (r - ((int)Math.Floor(r)));
            return result;
        }


        public double nextGaussian()
        {
            double result = 0.0;

            double d = this.nrandom();
            result = (d / 3.0 - 0.5);
            return result;
        }


        public int nextInt(int n)
        {
            int result = 0;
            double d = this.nextDouble();
            result = ((int) Math.Floor((d * n)));
            return result;
        }


        public int nextInt()
        {
            int result = 0;

            result = (int) this.nextInt(2147483647);
            return result;
        }


        public long nextLong()
        {
            long result = 0;

            double d = this.nextDouble();
            result = ((long) Math.Floor((d * 9223372036854775807L)));
            return result;
        }


        public bool nextBoolean()
        {
            bool result = false;

            double d = this.nextDouble();
            if (d > 0.5)
            {
                result = true;
            }
            return result;
        }


        public static ArrayList randomiseSequence(ArrayList sq)
        {
            Random r = new Random();
            ArrayList res = new ArrayList();
            ArrayList old = new ArrayList();
            old.AddRange(sq);
            while (old.Count > 0)
            {
                int x = old.Count;
                if (x == 1)
                {
                    res.Add(old[0]);
                    return res;
                }
                int n = r.Next(x);
                object obj = old[n];
                res.Add(obj);
                old.RemoveAt(n);
            }
            return res;
        }


        public static String randomString(int n)
        {
            Random r = new Random();

            String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$";
            String res = "";
            for (int i = 0; i < n; i++)
            {
                int code = (int) Math.Floor(r.NextDouble() * 54);
                res = res + characters[code];
            }
            return res;
        }

        public static Object randomElement(ArrayList col)
        {
            Random r = new Random();

            if (col.Count == 0)
            { return null; }
            int n = col.Count;
            int ind = (int) Math.Floor(r.NextDouble() * n);
            return col[ind];
        }

    }
