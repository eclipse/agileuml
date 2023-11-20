import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import java.util.function.Function;


class FinanceLib { 
  static ArrayList<FinanceLib> FinanceLib_allInstances = new ArrayList<FinanceLib>();

  FinanceLib() { FinanceLib_allInstances.add(this); }

  static FinanceLib createFinanceLib() { FinanceLib result = new FinanceLib();
    return result; }


  public static double discountDiscrete(double amount, double rate, double time)
  { double result = 0.0;
    if ((rate <= -1 || time < 0))
    { return result; }
  
    result = amount / Math.pow((1 + rate), time);
    return result;
  }


  public static double netPresentValueDiscrete(double rate, ArrayList<Double> values)
  { double result = 0.0;
    if ((rate <= -1))
    { return result; }
  
    int upper = values.size();
    
    for (int i = 0; i < upper; i++)
    { Object val = values.get(i);
      double dval = 0.0;  
      if (val instanceof Double)
      { dval = (double) val; } 
      else if (val instanceof Integer)
      { dval = 1.0*((int) val); } 
      result = result + FinanceLib.discountDiscrete(dval, 
                                          rate, i); 
    }
    return result;
  }


  public static double presentValueDiscrete(double rate, ArrayList<Double> values)
  {
    double result = 0;
    result = 0.0;
    if ((rate <= -1))
    { return result; }
  
    int upper = values.size();
    
    for (int i = 0; i < upper; i++)
    { Object val = values.get(i);
      double dval = 0.0;  
      if (val instanceof Double)
      { dval = (double) val; } 
      else if (val instanceof Integer)
      { dval = 1.0*((int) val); } 
      result = result + FinanceLib.discountDiscrete(dval, 
                                          rate, i+1); 
    }
    return result;
  }

  public static double bisectionDiscrete(double r, double rl, double ru, 
                                         ArrayList<Double> values)
  { double result = 0;
    result = 0;
    if ((r <= -1 || rl <= -1 || ru <= -1))
    { return result; }
  
    double v = 0;
    v = FinanceLib.netPresentValueDiscrete(r,values);
    if (ru - rl < MathLib.defaultTolerance)
    { return r; } 
    if (v > 0)
    { return FinanceLib.bisectionDiscrete((ru + r) / 2, r, ru, values); } 
    else if (v < 0)
    { return FinanceLib.bisectionDiscrete((r + rl) / 2, rl, r, values); }
    return r; 
  }

  public static double irrDiscrete(ArrayList<Double> values)
  { double res = FinanceLib.bisectionDiscrete(0.1,-0.5,1.0,values); 
    return res; 
  }

  public static ArrayList<OclDate> straddleDates(OclDate d1, OclDate d2, int period)
  {
    ArrayList<OclDate> result = new ArrayList<OclDate>();
    OclDate cd = d1;
    while (cd.compareToYMD(d2) <= 0)
    {
      cd = cd.addMonthYMD(period);
    }
    return Ocl.initialiseSequence(cd.subtractMonthYMD(period),cd);
  }


  public static int numberOfPeriods(OclDate settle, OclDate matur, int period)
  {
    int result = 0;
    double monthsToMaturity = OclDate.differenceMonths(matur, settle) * 1.0;
    return ((int) Math.ceil((monthsToMaturity / period)));
  }


  public static ArrayList<Integer> sequenceOfPeriods(OclDate sett, OclDate mat, int period)
  {
    ArrayList<Integer> result = new ArrayList<Integer>();
    int numPeriods = FinanceLib.numberOfPeriods(sett, mat, period);
    return Ocl.integerSubrange(1,numPeriods);
  }


  public static ArrayList<OclDate> couponDates(OclDate matur, int period, int numPeriods)
  {
    ArrayList<OclDate> result = new ArrayList<OclDate>();
    ArrayList<OclDate> cpdates = Ocl.initialiseSequence(matur);
    OclDate cpdate = matur;
    for (int i : Ocl.integerSubrange(0,numPeriods - 2))
    {
      int mo = cpdate.getMonth() - period;
      int prevMonth = mo;
      int prevYear = cpdate.getYear();
      int prevDay = cpdate.getDate();
      if (mo <= 0)
      {
        prevMonth = 12 + mo;
        prevYear = cpdate.year - 1;
      }
    
      cpdate = OclDate.newOclDate_YMD(prevYear, prevMonth, prevDay);
      cpdates = Ocl.append(cpdates,cpdate);
    }
    cpdates = Ocl.reverse(cpdates);
    return cpdates;
  }


  public static int days360(OclDate d1, OclDate d2, String num, OclDate mat)
  {
    int result = 0;
    int dd1 = d1.getDate();
    int dd2 = d2.getDate();
    int mm1 = d1.getMonth();
    int mm2 = d2.getMonth();
    int yy1 = d1.getYear();
    int yy2 = d2.getYear();
    if (num.equals("30/360"))
    {
      return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
    }
    
	if (num.equals("30/360B"))
    {
      dd1 = Ocl.min(Ocl.initialiseSet(dd1,30));
      if (dd1 > 29)
      {
        dd2 = Ocl.min(Ocl.initialiseSet(dd2,30));
      }
      return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
    }

    if (num.equals("30/360US"))
    {
      if (mm1 == 2 && (dd1 == 28 || dd1 == 29) && mm2 == 2 && (dd2 == 28 || dd2 == 29))
      {
        dd2 = 30;
      }
    
	  if (mm1 == 2 && (dd1 == 28 || dd1 == 29))
      {
        dd1 = 30;
      }

      if (dd2 == 31 && (dd1 == 30 || dd1 == 31))
      {
        dd2 = 30;
      }

      if (dd1 == 31)
      {
        dd1 = 30;
      }

      return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
    }
  
    if (num.equals("30E/360"))
    {
      if (dd1 == 31)
      {
        dd1 = 30;
      }
    
	  if (dd2 == 31)
      {
        dd2 = 30;
      }

      return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
    }

    if (num.equals("30E/360ISDA"))
    {
      if (d1.isEndOfMonth())
      {
        dd1 = 30;
      }

      if (!((d2.equals(mat) && mm2 == 2) && d2.isEndOfMonth()))
      {
        dd2 = 30;
      }
    
      return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
    }
    else {
      return 360 * (yy2 - yy1) + 30 * (mm2 - mm1) + (dd2 - dd1);
    }
  }


  public static ArrayList<Double> numberOfMonths(OclDate pd, OclDate settle, double cd, String dayCount, OclDate matur)
  {
    ArrayList<Double> result = new ArrayList<Double>();
    double sv = 0.0;
    if (dayCount.equals("Actual/360") || dayCount.equals("Actual/365F") || dayCount.equals("Actual/ActualICMA") || 
        dayCount.equals("Actual/364") || dayCount.equals("Actual/ActualISDA"))
    {
      int daysBetween = OclDate.daysBetweenDates(pd, settle);
      sv = (cd - daysBetween) / cd;
      return Ocl.initialiseSequence(sv,cd - daysBetween);
    }
    else {
      int daysBetween360 = FinanceLib.days360(pd, settle, dayCount, matur);
      sv = (cd - daysBetween360) / cd;
      return Ocl.initialiseSequence(sv,cd - daysBetween360); 
	}
  }


  public static ArrayList<ArrayList<Double>> calculateCouponPayments(ArrayList<OclDate> paymentDates, double annualCouponRate, String dayCountC, int freq)
  {
    ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
    ArrayList<Double> coupon_payments = (new ArrayList());
    ArrayList<Double> dates_payments = (new ArrayList());
    double cum_days = 0.0;
    double days = 0.0;
    for (int i : Ocl.integerSubrange(1,paymentDates.size() - 1))
    {
      OclDate startDate = ((OclDate) paymentDates.get(i - 1));
      OclDate endDate = ((OclDate) paymentDates.get(i + 1 - 1));
      if (dayCountC.equals("30/360") || dayCountC.equals("30/360B") || dayCountC.equals("30/360US") || dayCountC.equals("30E/360") || 
          dayCountC.equals("30E/360ISDA") || dayCountC.equals("Actual/360"))
      {
        days = FinanceLib.days360(startDate, endDate, dayCountC, Ocl.last(paymentDates));
      }
      else {
        if (dayCountC.equals("Actual/365F"))
        {
          days = 365.0 / freq;
        }
        else {
          if (dayCountC.equals("Actual/364"))
          {
            days = 364.0 / freq;
          }
          else {
            days = OclDate.daysBetweenDates(startDate, endDate);
          }
        }
      }
      double coupon_payment = annualCouponRate / freq;
      coupon_payments = Ocl.append(coupon_payments,coupon_payment);
      cum_days = cum_days + days;
      dates_payments = Ocl.append(dates_payments,cum_days);
    }
	
    return Ocl.initialiseSequence(coupon_payments,dates_payments);
  }


  public static ArrayList<Object> bondCashFlows(OclDate settle, OclDate matur, double coupon, String dayCount, int freq)
  {
    ArrayList<Object> result = new ArrayList<Object>();
    int period = ((int) (12 / freq));
    int np = FinanceLib.numberOfPeriods(settle, matur, period);
    ArrayList<Integer> snp = FinanceLib.sequenceOfPeriods(settle, matur, period);
    ArrayList<OclDate> cd = FinanceLib.couponDates(matur, period, np);
    OclDate pm = ((OclDate) (cd).get(1 - 1)).subtractMonthYMD(period);
    ArrayList<OclDate> cdn = Ocl.union(Ocl.initialiseSequence(pm),cd);
    ArrayList<ArrayList<Double>> coupPayments = FinanceLib.calculateCouponPayments(cdn, coupon, dayCount, freq);
    ArrayList<Double> cumd = ((ArrayList<Double>) coupPayments.get(2 - 1));
    ArrayList<Double> cp = ((ArrayList<Double>) coupPayments.get(1 - 1));
    ArrayList<Double> nm = FinanceLib.numberOfMonths(pm, settle, ((double) cumd.get(1 - 1)), dayCount, matur);
    if (settle.compareToYMD(pm) == 0)
    {
      return Ocl.initialiseSequence(cp,cd,snp,cumd);
    }
    else {
      ArrayList<Double> newsnp = Ocl.collectSequence(snp,(x)->{return x - (((int) snp.get(1 - 1)) - ((double) nm.get(1 - 1)));});
      ArrayList<Double> newcumd = Ocl.collectSequence(cumd,(x)->{return x - (((double) cumd.get(1 - 1)) - ((double) nm.get(2 - 1)));});
      return Ocl.initialiseSequence(cp,cd,newsnp,newcumd);
	}
  }


  public static double bondPrice(double yld, OclDate settle, OclDate matur, double coup, String dayCount, int freq)
  {
    double result = 0.0;
    ArrayList<Object> bcfs = FinanceLib.bondCashFlows(settle, matur, coup, dayCount, freq);
    ArrayList<Double> coupRates = ((ArrayList<Double>) bcfs.get(1 - 1));
    ArrayList<Double> timePoints = ((ArrayList<Double>) bcfs.get(3 - 1));
    ArrayList<Double> discountFactors = Ocl.collectSequence(timePoints,(x)->{return Math.pow((1.0 / (1 + (yld / freq))),x);});
    coupRates = Ocl.append(Ocl.front(coupRates),Ocl.last(coupRates) + 1);
    double sp = 0.0;
    for (int i : Ocl.integerSubrange(1,coupRates.size()))
    {
      sp = sp + (((double) discountFactors.get(i - 1)) * ((double) coupRates.get(i - 1)));
    }
    return sp;
  }


  public static double accInterest(OclDate issue, OclDate settle, int freq, double coup)
  {
    double result = 0.0;
    int period = ((int) (12 / freq));
    ArrayList<OclDate> st = FinanceLib.straddleDates(issue, settle, period);
    double aif = (1.0 * OclDate.daysBetweenDates(((OclDate) st.get(1 - 1)), settle)) / OclDate.daysBetweenDates(((OclDate) st.get(1 - 1)), ((OclDate) st.get(2 - 1)));
    return aif * (coup / freq);
  }


  public static double accumulatedInterest(OclDate issue, OclDate settle, int freq, double coup, String dayCount, OclDate matur)
  {
    double result = 0.0;
    int period = ((int) (12 / freq));
    ArrayList<OclDate> st = FinanceLib.straddleDates(issue, settle, period);
    OclDate d1 = ((OclDate) st.get(1 - 1));
    OclDate d2 = ((OclDate) st.get(2 - 1));
    int ys = d1.getYear();
    int ye = settle.getYear();
    OclDate ysEnd = OclDate.newOclDate_YMD(ys, 12, 31);
    OclDate yeStart = OclDate.newOclDate_YMD(ye, 1, 1);
    if (dayCount.equals("Actual/365F"))
    {
      return coup * (OclDate.daysBetweenDates(d1, settle) / 365.0);
    }

    if (dayCount.equals("Actual/ActualISDA"))
    {
      if (d1.isLeapYear() && settle.isLeapYear())
      {
        return coup * (OclDate.daysBetweenDates(d1, settle) / 366.0);
      }
      else {
        if ((!((d1.isLeapYear()) && !((settle.isLeapYear())))))
        {
          return coup * (OclDate.daysBetweenDates(d1, settle) / 365.0);
        }
        else {
          if ((d1.isLeapYear() && !((settle.isLeapYear()))))
          {
            return coup * (OclDate.daysBetweenDates(d1, ysEnd) / 366.0) + coup * (OclDate.daysBetweenDates(yeStart, settle) / 365.0);
          }
          else {
            return coup * (OclDate.daysBetweenDates(d1, ysEnd) / 365.0) + coup * (OclDate.daysBetweenDates(yeStart, settle) / 366.0);
          }
        }
      }
    }

    if (dayCount.equals("Actual/364"))
    {
      return coup * (OclDate.daysBetweenDates(d1, settle) / 364.0);
    }

    if (dayCount.equals("Actual/360"))
    {
      return coup * (OclDate.daysBetweenDates(d1, settle) / 360.0);
    }

    if (dayCount.equals("Actual/ActualICMA"))
    {
      return coup * (1.0 * OclDate.daysBetweenDates(d1, settle)) / (freq * OclDate.daysBetweenDates(d1, d2));
    }
    else {
      return coup * (FinanceLib.days360(d1, settle, dayCount, matur) / 360.0);
    }
  }


  public static double bondPriceClean(double Y, OclDate I, OclDate S, OclDate M, double c, String dcf, int f)
  {
    double result = 0.0;
    result = FinanceLib.bondPrice(Y, S, M, c, dcf, f) - FinanceLib.accumulatedInterest(I, S, f, c, dcf, M);
    return result;
  }
 
}

