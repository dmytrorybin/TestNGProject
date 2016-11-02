package ua.app.businessObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The class <code>AmountInWords</code> is used to format digital form of currency amounts
 * into the word form. This class is thread safe.
 *
 * Example of usage:
 * <code>AmountInWords.format(125012, AmountInWords.USD);</code>
 *
 * Nested class Currency is mainly used to provide morphology information.
 * Predefined set of currencies is: UAH, EUR, USD, RUB. Default currency is UAH. In order to change default
 * currency the method {@link businessObject.AmountInWords#setDefaultCurrency(businessObject.AmountInWords.Currency)} could be used.
 * New currencies could be added by using {@link businessObject.AmountInWords#addCurrency(businessObject.AmountInWords.Currency)}
 * method.
 *
 * In order to easily integrate AmountInWords class into the application which already has Currency class defined
 * there is an extension point provided by {@link businessObject.AmountInWords.CurrencyMapping} interface. Concrete CurrencyMapping
 * implementation could be set by {@link businessObject.AmountInWords#setCurrencyMapping(businessObject.AmountInWords.CurrencyMapping)} method.
 */
public final class AmountInWords {
    /**
     * Class which represents Currency.
     * Fields other than <code>code</code> and <code>name</code> are used for morphology.
     */
    public static class Currency {
        /** Digit currency code */
        protected int code;

        /** Symbol currency code */
        protected String name;

        // integer part morphology variables
        protected String oneInteger, twoIntegers, fiveIntegers;
        protected Sex integerSex;

        // fraction part morphology variables
        protected String oneFraction, twoFractions, fiveFractions;
        protected Sex fractionSex;


        public static Currency byCode(int code) {
            for (Currency c : currencies)
                if (c.getCode() == code)
                if (c.getCode() == code)
                    return c;
            return null;
        }

        public static Currency byCode(String code) {
            return byCode(Integer.parseInt(code));
        }

        public static Currency byName(String name) {
            for (Currency c : currencies)
                if (c.getName().equals(name))
                    return c;
            return null;
        }

        public Currency(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() { return code; }
        public String getName() { return name; }

        public boolean equals(Object o) {
            return (o instanceof Currency) && code == ((Currency) o).code;
        }
        public int hashCode() {
            return code;
        }

        public String toString() {
            return code + ":" + name;
        }
    }

    public static enum Sex { MALE, FEMALE }
    
    /**
     * Currency mapping interface.
     * Used in order to provide ability for class user to use custom currency implementation.
     * @param <T>
     */
    public static interface CurrencyMapping<T> {
        Currency getCurrency(T currency);
    }

    /**
     * Default implementation of currency mapping for working with currency implementation
     * provided by this class.
     */
    private static final class DefaultCurrencyMapping implements CurrencyMapping<Currency> {
        public Currency getCurrency(Currency currency) {
            return currency;
        }
    }


    public static final Currency UAH = new Currency(980, "UAH") {{
        oneInteger = ONE_UAH_INEGER;
        twoIntegers = TWO_UAH_INTEGER;
        fiveIntegers = FIVE_UAH_INTEGER;
        integerSex = Sex.FEMALE;

        oneFraction = ONE_UAH_FRACTION;
        twoFractions = TWO_UAH_FRACTION;
        fiveFractions = FIVE_UAH_FRACTION;
        fractionSex = Sex.FEMALE;
    }};

    public static final Currency EUR = new Currency(978, "EUR") {{
        oneInteger = ONE_EUR_INTEGER;
        twoIntegers = TWO_EUR_INTEGER;
        fiveIntegers = FIVE_EUR_INTEGER;
        integerSex = Sex.MALE;

        oneFraction = ONE_EUR_FRACTION;
        twoFractions = TWO_EUR_FRACTION;
        fiveFractions = FIVE_EUR_FRACTION;
        fractionSex = Sex.MALE;
    }};

    public static final Currency USD = new Currency(840, "USD") {{
        oneInteger = ONE_USD_INTEGER;
        twoIntegers = TWO_USD_INTEGER;
        fiveIntegers = FIVE_USD_INTEGER;
        integerSex = Sex.MALE;

        oneFraction = ONE_USD_FRACTION;
        twoFractions = TWO_USD_FRACTION;
        fiveFractions = FIVE_USD_FRACTION;
        fractionSex = Sex.MALE;
    }};

    public static final Currency RUB = new Currency(643, "RUB") {{
        oneInteger = ONE_RUB_INTEGER;
        twoIntegers = TWO_RUB_INTEGER;
        fiveIntegers = FIVE_RUB_INTEGER;
        integerSex = Sex.MALE;

        oneFraction = ONE_RUB_FRACTION;
        twoFractions = TWO_RUB_FRACTION;
        fiveFractions = FIVE_RUB_FRACTION;
        fractionSex = Sex.FEMALE;
    }};


    /** Collection which holds all registered currencies. */
    private static final List<Currency> currencies = new CopyOnWriteArrayList<Currency>() {{
        add(UAH); add(EUR); add(USD); add(RUB);
    }};

    /**
     * Register new currency within AmountInWords class.
     * Method performes primitive validation in order to prevent common errors in future. 
     * @param currency currency to be registered
     * @throws NullPointerException in case any field in Currency is null
     */
    public static void addCurrency(Currency currency) {
        if (currency == null) throw new NullPointerException("Currency is null");

        if (currency.oneInteger == null
                || currency.twoIntegers == null
                || currency.fiveIntegers == null
                || currency.integerSex == null
                || currency.oneFraction == null
                || currency.twoFractions == null
                || currency.fiveFractions == null
                || currency.fractionSex == null)
            throw new NullPointerException("Currency " + currency + " is not properly initialized");

        for (Currency c : currencies)
            if (c.getCode() == currency.getCode() || c.getName().equals(currency.getName()))
                throw new IllegalStateException("Currency " + currency + "already registered");

        // make copy just to be sure that somebody will not break our contract
        Currency copy = new Currency(currency.getCode(), currency.getName());
        copy.oneInteger = currency.oneInteger;
        copy.twoIntegers = currency.twoIntegers;
        copy.fiveIntegers = currency.fiveIntegers;
        copy.integerSex = currency.integerSex;
        copy.oneFraction = currency.oneFraction;
        copy.twoFractions = currency.twoFractions;
        copy.fiveFractions = currency.fiveFractions;
        copy.fractionSex = currency.fractionSex;

        currencies.add(copy);
    }
    public static void removeCurrency(Currency currency) { currencies.remove(currency); }
    public static List<Currency> getCurrencies() { return new ArrayList<Currency>(currencies); }


    /**
     * Default currency to be used when method AmountInWords.format(long) is called.
     * @see businessObject.AmountInWords#format(long)
     */
    private static volatile Currency defaultCurrency = UAH;

    public static Currency getDefaultCurrency() { return defaultCurrency; }
    /**
     * Set default currency.
     * @param currency currency to be used by default
     * @throws IllegalArgumentException thrown when passing null as parameter
     */
    public static void setDefaultCurrency(Currency currency) {
        if (currency == null)
            throw new IllegalArgumentException("Default currency should not be null");
        defaultCurrency = currency;
    }


    private static volatile CurrencyMapping<?> currencyMapping = new DefaultCurrencyMapping();

    public static CurrencyMapping getCurrencyMapping() { return currencyMapping; }
    /**
     * Set custom currency mapping implementation.
     * When parameter is null then currencyMapping is set to DefaultCurrencyMapping instance.
     * @param mapping currency mapping to be used
     */
    public static void setCurrencyMapping(CurrencyMapping mapping) {
        if (mapping == null) currencyMapping = new DefaultCurrencyMapping();
        else currencyMapping = mapping;
    }

    
    /**
     * Format amount in words using default currency.
     * @param amount amount to be formatted
     * @return amount in words
     * @see businessObject.AmountInWords#format(long, Object)
     */
    public static String format(long amount) {
        return format(amount, defaultCurrency);
    }

    /**
     * Format amount in words using the currecy specified.
     * @param amount amount to be formatted
     * @param currency currency to be used
     * @return amount formatted in words as string
     */
    public static <T> String format(long amount, T currency) {
        if (amount > 99999999999999999L || amount < -99999999999999999L)
            throw new UnsupportedOperationException("Amounts grater than 999'999'999'999'999.00 are not supported.");

        @SuppressWarnings({"unchecked"})
        Currency c = getCurrencyMapping().getCurrency(currency);

        if (c == null)
            throw new IllegalArgumentException("Currency " + currency + " is not found");

        boolean notEmpty = (amount / 100 / 1000 ) == 0;

        Triad integerUnits = new Triad(c.oneInteger, c.twoIntegers, c.fiveIntegers, c.integerSex, notEmpty);
        Triad fractionUnits = new Triad(c.oneFraction, c.twoFractions, c.fiveFractions, c.fractionSex, true) {
            @Override int getTriadFromAmount(long amount) {
                return (int) (amount % 100);
            }
        };

        List<Triad> triads = Arrays.asList(Triad.TRILLION, Triad.BILLION, Triad.MILLION, Triad.THOUSAND,
                integerUnits, fractionUnits);

        StringBuilder amountInWords = new StringBuilder();
        for (Triad triad : triads)
            amountInWords.append(triadToWord(triad, triad.getTriadFromAmount(amount)));

        return amountInWords.toString();
    }

    
    private static String triadToWord(Triad triad, int value) {
        StringBuilder builder = new StringBuilder();

        if (value == 0) {
            if (!triad.mandatory) return "";
            
            if (triad.zero) return WORD_0 + " " + ending(triad, value);
            else return ending(triad, value);
        }

        int hundreds = value / 100;
        int tens = (value % 100) / 10;
        int units = value % 10;

        switch (hundreds) {
            default: break;
            case 1:  builder.append(WORD_100); break;
            case 2:  builder.append(WORD_200); break;
            case 3:  builder.append(WORD_300); break;
            case 4:  builder.append(WORD_400); break;
            case 5:  builder.append(WORD_500); break;
            case 6:  builder.append(WORD_600); break;
            case 7:  builder.append(WORD_700); break;
            case 8:  builder.append(WORD_800); break;
            case 9:  builder.append(WORD_900); break;
        }
        if (hundreds > 0) builder.append(' ');

        switch (tens) {
            default: break;
            case 2:  builder.append(WORD_20); break;
            case 3:  builder.append(WORD_30); break;
            case 4:  builder.append(WORD_40); break;
            case 5:  builder.append(WORD_50); break;
            case 6:  builder.append(WORD_60); break;
            case 7:  builder.append(WORD_70); break;
            case 8:  builder.append(WORD_80); break;
            case 9:  builder.append(WORD_90); break;
        }

        if (tens == 1) {
            switch (units) {
                case 0: builder.append(WORD_10); break;
                case 1: builder.append(WORD_11); break;
                case 2: builder.append(WORD_12); break;
                case 3: builder.append(WORD_13); break;
                case 4: builder.append(WORD_14); break;
                case 5: builder.append(WORD_15); break;
                case 6: builder.append(WORD_16); break;
                case 7: builder.append(WORD_17); break;
                case 8: builder.append(WORD_18); break;
                case 9: builder.append(WORD_19); break;
            }
        }
        if (tens > 0) builder.append(' ');

        if (tens != 1) {
            switch (units) {
                default: break;
                case 1:
                    builder.append(triad.sex.equals(Sex.MALE) ? WORD_1_MALE : WORD_1_FEMALE);
                    break;
                case 2:
                    builder.append(triad.sex.equals(Sex.MALE) ? WORD_2_MALE : WORD_2_FEMALE);
                    break;
                case 3: builder.append(WORD_3); break;
                case 4: builder.append(WORD_4); break;
                case 5: builder.append(WORD_5); break;
                case 6: builder.append(WORD_6); break;
                case 7: builder.append(WORD_7); break;
                case 8: builder.append(WORD_8); break;
                case 9: builder.append(WORD_9); break;
            }
            if (units > 0) builder.append(' ');
        }

        builder.append(ending(triad, value));

        return builder.toString();
    }

    private static String ending(Triad triad, int value) {
        int tens = (value % 100) / 10;
        int units = value % 10;

        if (tens == 1) return triad.five + " ";

        String ending;
        switch (units) {
            default: ending = triad.five; break;
            case 1:  ending = triad.one; break;
            case 2:
            case 3:
            case 4:  ending = triad.two; break;
        }
        return ending + " ";
    }


    private static class Triad {
        static final Triad THOUSAND = new Triad(THOUSAND_ONE, THOUSAND_TWO, THOUSAND_FIVE, Sex.FEMALE, 3);
        static final Triad MILLION = new Triad(MILLION_ONE, MILLION_TWO, MILLION_FIVE, Sex.MALE, 6);
        static final Triad BILLION = new Triad(BILLION_ONE, BILLION_TWO, BILLION_FIVE, Sex.MALE, 9);
        static final Triad TRILLION = new Triad(TRILLION_ONE, TRILLION_TWO, TRILLION_FIVE, Sex.MALE, 12);

        private Triad(String one, String two, String five, Sex sex, long divisor) {
            this (one, two, five, sex, false, false, divisor);
        }

        private Triad(String one, String two, String five, Sex sex, boolean zero) {
            this (one, two, five, sex, true, zero, 0);
        }

        private Triad(String one, String two, String five, Sex sex, boolean mandatory, boolean zero, long power) {
            this.one = one;
            this.two = two;
            this.five = five;
            this.sex = sex;
            this.mandatory = mandatory;
            this.zero = zero;
            this.power = power;
        }

        String one;
        String two;
        String five;
        Sex sex;
        // triad must be present in word representation
        boolean mandatory;
        // zero triad value should not be omitted but represented as 0 instead
        boolean zero;
        long power = 0;

        int getTriadFromAmount(long amount) {
            long divisor = (long) Math.pow(10, power + 2);
            return (int) (amount / divisor % 1000);
        }
    }

    /** Default private constructor to avoid explicit instance creation. */
    private AmountInWords() { }
    
    // this constants could be changed in order to use another language
    private static final String ONE_UAH_INEGER = "гривна";
    private static final String TWO_UAH_INTEGER = "гривны";
    private static final String FIVE_UAH_INTEGER = "гривен";
    private static final String ONE_UAH_FRACTION = "копейка";
    private static final String TWO_UAH_FRACTION = "копейки";
    private static final String FIVE_UAH_FRACTION = "копеек";

    private static final String ONE_EUR_INTEGER = "евро";
    private static final String TWO_EUR_INTEGER = "евро";
    private static final String FIVE_EUR_INTEGER = "евро";
    private static final String ONE_EUR_FRACTION = "евроцент";
    private static final String TWO_EUR_FRACTION = "евроцента";
    private static final String FIVE_EUR_FRACTION = "евроцентов";

    private static final String ONE_USD_INTEGER = "доллар";
    private static final String TWO_USD_INTEGER = "доллара";
    private static final String FIVE_USD_INTEGER = "долларов";
    private static final String ONE_USD_FRACTION = "цент";
    private static final String TWO_USD_FRACTION = "цента";
    private static final String FIVE_USD_FRACTION = "центов";

    private static final String ONE_RUB_INTEGER = "рубль";
    private static final String TWO_RUB_INTEGER = "рубля";
    private static final String FIVE_RUB_INTEGER = "рублей";
    private static final String ONE_RUB_FRACTION = "копейка";
    private static final String TWO_RUB_FRACTION = "копейки";
    private static final String FIVE_RUB_FRACTION = "копеек";

    private static final String WORD_100 = "сто";
    private static final String WORD_200 = "двести";
    private static final String WORD_300 = "триста";
    private static final String WORD_400 = "четыреста";
    private static final String WORD_500 = "пятьсот";
    private static final String WORD_600 = "шестьсот";
    private static final String WORD_700 = "семьсот";
    private static final String WORD_800 = "восемьсот";
    private static final String WORD_900 = "девятьсот";

    private static final String WORD_20 = "двадцать";
    private static final String WORD_30 = "тридцать";
    private static final String WORD_40 = "сорок";
    private static final String WORD_50 = "пятьдесят";
    private static final String WORD_60 = "шестьдесят";
    private static final String WORD_70 = "семьдесят";
    private static final String WORD_80 = "восемьдесят";
    private static final String WORD_90 = "девяносто";

    private static final String WORD_10 = "десять";
    private static final String WORD_11 = "одиннадцать";
    private static final String WORD_12 = "двенадцать";
    private static final String WORD_13 = "тринадцать";
    private static final String WORD_14 = "четырнадцать";
    private static final String WORD_15 = "пятнадцать";
    private static final String WORD_16 = "шестнадцать";
    private static final String WORD_17 = "семнадцать";
    private static final String WORD_18 = "восемнадцать";
    private static final String WORD_19 = "девятнадцать";

    private static final String WORD_0 = "ноль";
    private static final String WORD_1_MALE = "один";
    private static final String WORD_1_FEMALE = "одна";
    private static final String WORD_2_MALE = "два";
    private static final String WORD_2_FEMALE = "две";
    private static final String WORD_3 = "три";
    private static final String WORD_4 = "четыре";
    private static final String WORD_5 = "пять";
    private static final String WORD_6 = "шесть";
    private static final String WORD_7 = "семь";
    private static final String WORD_8 = "восемь";
    private static final String WORD_9 = "девять";

    private static final String THOUSAND_ONE = "тысяча";
    private static final String THOUSAND_TWO = "тысячи";
    private static final String THOUSAND_FIVE = "тысяч";

    private static final String MILLION_ONE = "миллион";
    private static final String MILLION_TWO = "миллиона";
    private static final String MILLION_FIVE = "миллионов";

    private static final String BILLION_ONE = "миллиард";
    private static final String BILLION_TWO = "миллиарда";
    private static final String BILLION_FIVE = "миллиардов";

    private static final String TRILLION_ONE = "триллион";
    private static final String TRILLION_TWO = "триллиона";
    private static final String TRILLION_FIVE = "триллионов";
}
