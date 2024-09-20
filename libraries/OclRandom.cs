
    public class Pcg
    {

/*
 * PCG Random Number Generation for C#.
 *
 * Copyright 2020-2021 Terra Lauterbach <potatointeractive@gmail.com>
 * Copyright 2015 Kevin Harris <kevin@studiotectorum.com>
 * Copyright 2014 Melissa O'Neill <oneill@pcg-random.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For additional information about the PCG random number generation scheme,
 * including its license and other licensing options, visit
 *
 *     http://www.pcg-random.org
 */

		#region Private internal state
		/// <summary>
		/// The RNG state. All values are possible.
		/// </summary>
		protected ulong m_state;

		/// <summary>
		/// Controls which RNG sequence (stream) is selected.
		/// Must <strong>always</strong> be odd.
		/// </summary>
		protected ulong m_inc;
		#endregion

		/// <summary>
		/// Initializes a new instance of the <see cref="Pcg"/> class
		/// <strong>FOR TESTING</strong> with a <strong>KNOWN</strong> seed.
		/// </summary>
		public Pcg()
		{
			Seed(0x853c49e6748fea9bUL, 0xda3e39cb94b95bdbUL);
		}

		/// <summary>
		/// Initializes a new instance of the <see cref="Pcg"/> class.
		/// </summary>
		/// <param name="initState">Initial state.</param>
		/// <param name="initSeq">Initial sequence</param>
		public Pcg(ulong initState, ulong initSeq)
		{
			Seed(initState, initSeq);
		}

		/// <summary>
		/// Seed Pcg in two parts, a state initializer
		/// and a sequence selection constant (a.k.a.
		/// stream id).
		/// </summary>
		/// <param name="initState">Initial state.</param>
		/// <param name="initSeq">Initial sequence</param>
		public void Seed(ulong initState, ulong initSeq)
		{
			m_state = 0U;
			m_inc = (initSeq << 1) | 1UL;
			Random32();
			m_state += initState;
			Random32();
		}

		/// <summary>
		/// Generates a uniformly-distributed 32-bit random number.
		/// </summary>
		public uint Random32()
		{
			ulong oldState = m_state;
			m_state = oldState * 6364136223846793005UL + m_inc;
			uint xorShifted = (uint)(((oldState >> 18) ^ oldState) >> 27);
			int rot = (int)(oldState >> 59);
			return (xorShifted >> rot) | (xorShifted << ((-rot) & 31));
		}

		/// <summary>
		/// Generates a uniformly distributed number, r,
		/// where 0 <= r < exclusiveBound.
		/// </summary>
		/// <param name="exlusiveBound">Exlusive bound.</param>
		public uint Range32(uint exclusiveBound)
		{
			// To avoid bias, we need to make the range of the RNG
			// a multiple of bound, which we do by dropping output
			// less than a threshold. A naive scheme to calculate the
			// threshold would be to do
			//
			//     uint threshold = 0x100000000UL % exclusiveBound;
			//
			// but 64-bit div/mod is slower than 32-bit div/mod 
			// (especially on 32-bit platforms). In essence, we do
			//
			//     uint threshold = (0x100000000UL - exclusiveBound) % exclusiveBound;
			//
			// because this version will calculate the same modulus,
			// but the LHS value is less than 2^32.
			uint threshold = (uint)((0x100000000UL - exclusiveBound) % exclusiveBound);

			// Uniformity guarantees that this loop will terminate.
			// In practice, it should terminate quickly; on average
			// (assuming all bounds are equally likely), 82.25% of
			// the time, we can expect it to require just one 
			// iteration. In the worst case, someone passes a bound
			// of 2^31 + 1 (i.e., 2147483649), which invalidates
			// almost 50% of the range. In practice bounds are
			// typically small and only a tiny amount of the range
			// is eliminated.
			for (; ; )
			{
				uint r = Random32();
				if (r >= threshold)
				{
					return r % exclusiveBound;
				}
			}
		}

		/// <summary>
		/// Generates a uniformly distributed number, r,
		/// where minimum <= r < exclusiveBound.
		/// </summary>
		/// <param name="minimum">The minimum inclusive value.</param>
		/// <param name="exclusiveBound">The maximum exclusive bound.</param>
		public int Range32(int minimum, int exclusiveBound)
		{
			uint boundRange = (uint)(exclusiveBound - minimum);
			uint rangeResult = Range32(boundRange);
			return (int)rangeResult + (int)minimum;
		}

		/// <summary>
		/// Generates a random boolean value
		/// </summary>
		public bool RandomBoolean()
		{
			return Range32(2) == 0;
		}

		/// <summary>
		/// Generates a float between 0.0 (inclusive) and 1.0 (inclusive)
		/// </summary>
		public float RandomFloat()
		{
			return Random32() / (float)uint.MaxValue;
		}

		/// <summary>
		/// Generates a float between the given inclusive min 
		/// and exclusive max
		/// </summary>
		public float RangeFloat(float inclusiveMin, float exclusiveMax)
		{
			return inclusiveMin + Random32() / (uint.MaxValue / (exclusiveMax - inclusiveMin));
		}

		/// <summary>
		/// Returns a <see cref="System.String"/> that represents the current <see cref="PcgRandom.Pcg"/>.
		/// </summary>
		/// <returns>A <see cref="System.String"/> that represents the current <see cref="PcgRandom.Pcg"/>.</returns>
		public override string ToString()
		{
			return string.Format("[Pcg state: {0}; sequence: {1}]", m_state, m_inc);
		}
	}

    class OclRandom
    {
        private int ix; // internal
        private int iy; // internal
        private int iz; // internal
        private string distribution; // internal
        private double bernoulliP; // internal
        private double normalMean; // internal
        private double normalVariance; // internal
        private double uniformLower; // internal
        private double uniformUpper; // internal
        private double poissonLambda; // internal

        private string algorithm = "LCG"; 
        Pcg pcg; 

          private static OclRandom _defaultInstanceOclRandom = null;

        public OclRandom()
        {
            ix = 0;
            iy = 0;
            iz = 0;
            distribution = "uniform";
            bernoulliP = 0.0;
            normalMean = 0.0;
            normalVariance = 1.0;
            uniformLower = 0.0;
            uniformUpper = 1.0;
            poissonLambda = 1.0;
        }

        public static OclRandom defaultInstanceOclRandom()
        {
            if (OclRandom._defaultInstanceOclRandom == null)
            {
                OclRandom._defaultInstanceOclRandom = OclRandom.newOclRandom();
            }
            return OclRandom._defaultInstanceOclRandom;
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

        public void setAlgorithm(string algo)
        { if ("PCG".Equals(algo))
          { pcg = new Pcg((ulong) SystemTypes.getTime() , 0xda3e39cb94b95bdbUL); } 
          algorithm = algo; 
        } 

       public void setdistribution(string distribution_x) { distribution = distribution_x; }

        public void setbernoulliP(double bernoulliP_x) { bernoulliP = bernoulliP_x; }

        public void setnormalMean(double normalMean_x) { normalMean = normalMean_x; }

        public void setnormalVariance(double normalVariance_x) { normalVariance = normalVariance_x; }

        public void setuniformLower(double uniformLower_x) { uniformLower = uniformLower_x; }


        public void setuniformUpper(double uniformUpper_x) { uniformUpper = uniformUpper_x; }

        public void setpoissonLambda(double poissonLambda_x) { poissonLambda = poissonLambda_x; }

public string getdistribution() { return distribution; }

        public double getbernoulliP() { return bernoulliP; }

        public double getnormalMean() { return normalMean; }

        public double getnormalVariance() { return normalVariance; }

        public double getuniformLower() { return uniformLower; }

        public double getuniformUpper() { return uniformUpper; }

        public double getpoissonLambda() { return poissonLambda; }


        public static OclRandom newOclRandom()
        {
            OclRandom result = null;

            OclRandom rd = new OclRandom();
            rd.setix(1001);
            rd.setiy(781);
            rd.setiz(913);
            rd.setdistribution("uniform");
            result = rd;
            return result;
        }

        public static OclRandom newOclRandom_PCG()
        {
            OclRandom result = null;

            OclRandom rd = new OclRandom();
            rd.setix(1001);
            rd.setiy(781);
            rd.setiz(913);
            rd.setdistribution("uniform");
            rd.algorithm = "PCG"; 
            rd.pcg = new Pcg((ulong) SystemTypes.getTime() , 0xda3e39cb94b95bdbUL); 
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
            rd.setdistribution("uniform");
            result = rd;
            return result;
        }

        public static OclRandom newOclRandom_Seed(long n)
        {
            OclRandom result = null;

            OclRandom rd = new OclRandom();
            rd.setix(((int)(n % 30269)));
            rd.setiy(((int)(n % 30307)));
            rd.setiz(((int)(n % 30323)));
            rd.setdistribution("uniform");
            result = (OclRandom)(rd);
            return result;
        }


        public static OclRandom newOclRandomBernoulli(double p)
        {
            OclRandom result = null;

            OclRandom rd = new OclRandom();
            rd.setix(1001);
            rd.setiy(781);
            rd.setiz(913);
            rd.setdistribution("bernoulli");
            rd.setbernoulliP(p);
            result = (OclRandom)(rd);
            return result;
        }


        public static OclRandom newOclRandomNormal(double mu, double vari)
        {
            OclRandom result = null;

            OclRandom rd = new OclRandom();
            rd.setix(1001);
            rd.setiy(781);
            rd.setiz(913);
            rd.setdistribution("normal");
            rd.setnormalMean(mu);
            rd.setnormalVariance(vari);
            result = (OclRandom)(rd);
            return result;
        }


        public static OclRandom newOclRandomUniform(double lwr, double upr)
        {
            OclRandom result = null;

            OclRandom rd = new OclRandom();
            rd.setix(1001);
            rd.setiy(781);
            rd.setiz(913);
            rd.setdistribution("uniform");
            rd.setuniformLower(lwr);
            rd.setuniformUpper(upr);
            result = (OclRandom)(rd);
            return result;
        }


        public static OclRandom newOclRandomPoisson(double lm)
        {
            OclRandom result = null;

            OclRandom rd = new OclRandom();
            rd.setix(1001);
            rd.setiy(781);
            rd.setiz(913);
            rd.setdistribution("poisson");
            rd.setpoissonLambda(lm);
            result = (OclRandom)(rd);
            return result;
        }

        public void setSeeds(int x, int y, int z)
        {
           setix(x);
           setiy(y);
           setiz(z);
            if ("PCG".Equals(algorithm))
              { pcg.Seed((ulong) x, (ulong) y); }
        }

        public void setSeed(long n)
        {
          setix((int)(n % 30269));
          setiy((int)(n % 30307));
          setiz((int)(n % 30323));
            if ("PCG".Equals(algorithm))
             { pcg.Seed((ulong)n, (ulong)n); }
      }

        public double nrandom()
        {
            setix((this.getix() * 171) % 30269);
            setiy((this.getiy() * 172) % 30307);
            setiz((this.getiz() * 170) % 30323);
            return (this.getix() / 30269.0 + this.getiy() / 30307.0 + this.getiz() / 30323.0);
        }


        public double nextDouble()
        {
          if ("PCG".Equals(algorithm))
            { return pcg.RandomFloat(); }

          double result = 0.0;
          double r = this.nrandom();
          result = (r - ((int)Math.Floor(r)));
          return result;
        }


        public double nextFloat()
        {
            if ("PCG".Equals(algorithm))
            { return pcg.RandomFloat(); }
      
          double result = 0.0;

          double r = this.nrandom();
          result = (r - ((int)Math.Floor(r)));
          return result;
        }


        public double nextGaussian()
        {
            return nextNormal(0,1);
        }


        public int nextInt(int n)
        {
           if ("PCG".Equals(algorithm))
             { return (int) pcg.Range32((uint) n); }

            int result = 0;
            double d = this.nextDouble();
            result = ((int) Math.Floor((d * n)));
            return result;
        }


        public int nextInt()
        {
            if ("PCG".Equals(algorithm))
            { return (int) pcg.Random32(); }

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
            if ("PCG".Equals(algorithm))
            { return pcg.RandomBoolean(); }

            bool result = false;

            double d = this.nextDouble();
            if (d > 0.5)
            {
                result = true;
            }
            return result;
        }

        public int nextBernoulli(double p)
        {
            int result = 0;

            double d = this.nextDouble();
            if (d > p)
            {
                result = 0;
            }
            else if (d <= p)
            {
                result = 1;
            }
            return result;
        }


        public double nextNormal(double mu, double vari)
        {
            double d = 0.0;

            int i = 0;

            while (i < 12)
            {
                d = d + this.nextDouble();
                i = i + 1;

            }
            d = d - 6;
            return mu + d * Math.Sqrt(vari);
        }

        public double nextUniform(double lwr, double upr)
        {
            double result = 0.0;
            double d = this.nextDouble();
            result = lwr + (upr - lwr) * d;
            return result;
        }

        public double nextPoisson(double lam)
        {
            double x = 0.0;
            double p = Math.Exp(-lam);
            double s = p;
            double u = this.nextDouble();

            while (u > s)
            {
                x = x + 1;
                p = p * lam / x;
                s = s + p;
            }
            return x;
        }

        public double next()
        {
            double result = 0.0;

            if (((string)distribution).Equals("normal"))
            {
                result = this.nextNormal(normalMean, normalVariance);
            }
            else
            {
                if (((string)distribution).Equals("bernoulli"))
                {
                    result = this.nextBernoulli(this.getbernoulliP());
                }
                else
                {
                    if (((string)distribution).Equals("uniform"))
                    {
                        result = this.nextUniform(this.getuniformLower(), this.getuniformUpper());
                    }
                    else
                    {
                        if (((string)distribution).Equals("poisson"))
                        {
                            result = this.nextPoisson(this.getpoissonLambda());
                        }
                        else
                        {
                            result = this.nextDouble();
                        }
                    }
                }
            }
            return result;
        }


        public double mean()
        {
            double result = 0.0;

            if (((string)distribution).Equals("normal"))
            {
                result = this.getnormalMean();
            }
            else
            {
                if (((string)distribution).Equals("bernoulli"))
                {
                    result = this.getbernoulliP();
                }
                else
                {
                    if (((string)distribution).Equals("uniform"))
                    {
                        result = (this.getuniformUpper() + this.getuniformLower()) / 2.0;
                    }
                    else
                    {
                        if (((string)distribution).Equals("poisson"))
                        {
                            result = this.getpoissonLambda();
                        }
                        else
                        {
                            result = 0.5;
                        }
                    }
                }
            }
            return result;
        }

        public double variance()
        {
            double result = 0.0;

            if (((string)distribution).Equals("normal"))
            {
                result = this.getnormalVariance();
            }
            else
            {
                if (((string)distribution).Equals("bernoulli"))
                {
                    result = this.getbernoulliP() * (1 - this.getbernoulliP());
                }
                else
                {
                    if (((string)distribution).Equals("uniform"))
                    {
                        result = (this.getuniformUpper() - this.getuniformLower()) / 12.0;
                    }
                    else
                    {
                        if (((string)distribution).Equals("poisson"))
                        {
                            result = this.getpoissonLambda();
                        }
                        else
                        {
                            result = 1.0 / 12.0;
                        }
                    }
                }
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

        public static ArrayList randomUniqueElements(ArrayList col, int n)
        {
            Random r = new Random(); 
            ArrayList res = new ArrayList(); 
            int sze = col.Count;
            while (res.Count < n)
            {
                int ind = (int) Math.Floor(r.NextDouble() * sze);
                object x = col[ind];
                if (res.Contains(x)) { }
                else
                { res.Add(x); }
            }
            return res;
        }

        public static ArrayList randomElements(ArrayList col, int n)
        {
            Random r = new Random(); 
            ArrayList res = new ArrayList(); 
            int sze = col.Count;
            while (res.Count < n)
            {
                int ind = (int) Math.Floor(r.NextDouble() * sze);
                object x = col[ind];
                res.Add(x);
            }
            return res;
        }

    public static ArrayList randomList(int n, OclRandom rd)
    { // Implements: Integer.subrange(1,sh->at(1))->collect( int_0_xx | rd.nextDouble() )

      ArrayList _results_0 = new ArrayList();
      for (int _i = 0; _i < n; _i++)
      { _results_0.Add(rd.nextDouble()); }

      return _results_0;
    }

        public static ArrayList randomMatrix(int n,
                                             ArrayList sh, OclRandom rd)
        { // Implements: Integer.subrange(1,sh->at(1))->collect( int_1_xx | OclRandom.randomValuesMatrix(sh->tail()) )

            ArrayList _results_1 = new ArrayList();
            for (int _i = 0; _i < n; _i++)
            {
                _results_1.Add(
                  OclRandom.randomValuesMatrix(
                                  SystemTypes.tail(sh), rd));
            }

            return _results_1;
        }

        public static ArrayList randomValuesMatrix(ArrayList sh)
        {
            OclRandom rd = OclRandom.newOclRandom_PCG();

            if (sh.Count == 0)
            { return (new ArrayList()); }

            if ((sh).Count == 1)
            { return OclRandom.randomList((int)sh[0], rd); }

            ArrayList res = OclRandom.randomMatrix((int)sh[0], sh, rd);
            return res;
        }

        public static ArrayList randomValuesMatrix(ArrayList sh, OclRandom rd)
        {
            if (sh.Count == 0)
            { return (new ArrayList()); }

            if ((sh).Count == 1)
            { return OclRandom.randomList((int)sh[0], rd); }

            ArrayList res = OclRandom.randomMatrix((int)sh[0], sh, rd);
            return res;
        }

}
