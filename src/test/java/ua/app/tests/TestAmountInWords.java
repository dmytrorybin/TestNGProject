package ua.app.tests;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.log4j.Logger;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import ua.app.base.TestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ua.app.businessObject.AmountInWords;
import ua.app.utilities.TestListener;

import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static ua.app.businessObject.AmountInWords.*;

/**
 * Created by Ievgen on 12.05.2016.
 */
@Listeners({TestListener.class})
public class TestAmountInWords extends TestBase {

    @Test(dataProvider = "dataProviderForAmount", groups = {"green"}, priority = 0)
    public void testFormatPlus(long number, String expected){
        String actual = AmountInWords.format(number);
        assertEquals(actual, expected);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test(dataProvider = "dataProviderForAmount", groups = {"green"}, priority = 1)
    public void testAddNewCurrency(long code, String name){
        AmountInWords.Currency yen = new AmountInWords.Currency((int)code, name) {{
            oneInteger = "иена";
            twoIntegers = "иены";
            fiveIntegers = "иен";
            integerSex = AmountInWords.Sex.FEMALE;
            oneFraction = "сэн";
            twoFractions = "сэн";
            fiveFractions = "сэн";
            fractionSex = AmountInWords.Sex.MALE;
        }};
        AmountInWords.addCurrency(yen);
        List<Currency> actual = AmountInWords.getCurrencies();

        assertThat(actual, Matchers.hasItem(yen));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForCurrencies", groups = {"green"}, priority = 2)
    public void testRemoveCurrency(int currencyNum1, int currencyNum2, int currencyNum3,int currencyNum4){
        AmountInWords.removeCurrency(AmountInWords.getCurrencies().get(currencyNum1));
        List<Currency> actaul = AmountInWords.getCurrencies();

        assertThat(actaul, Matchers.hasItems(AmountInWords.getCurrencies().get(currencyNum2),
                AmountInWords.getCurrencies().get(currencyNum3), AmountInWords.getCurrencies().get(currencyNum4)));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForAmount", groups = {"green"}, priority = 3, expectedExceptions = {IllegalStateException.class})
    public void testAddTwoNewCurrency(long code, String name, String one, String two, String three, String fraction){
        AmountInWords.Currency yen = new AmountInWords.Currency((int)code, name) {{
            oneInteger = one;
            twoIntegers = two;
            fiveIntegers = three;
            integerSex = AmountInWords.Sex.FEMALE;
            oneFraction = fraction;
            twoFractions = fraction;
            fiveFractions = fraction;
            fractionSex = AmountInWords.Sex.MALE;
        }};
        AmountInWords.addCurrency(yen);
        AmountInWords.addCurrency(yen);
        List<Currency> actual = AmountInWords.getCurrencies();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForCurrencies", groups = {"green"}, priority = 3, expectedExceptions = {AssertionError.class})
    public void testRemoveCurrencyNegative(int currencyNum){
        Currency currency = AmountInWords.getCurrencies().get(currencyNum);
        AmountInWords.removeCurrency(currency);
        List<Currency> actual = AmountInWords.getCurrencies();

        assertThat(actual, Matchers.hasItems(currency));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForAmount", groups = {"green"}, priority = 2, expectedExceptions = {IllegalArgumentException.class})
    public void testFormatOverflow(long number, String expected){
        String actual = AmountInWords.format(number);
        assertEquals(actual, expected);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForAmount", groups = {"green"}, priority = 2)
    public void testFormatLowerBorder(long number, String expected){
        String actual = AmountInWords.format(number);
        assertEquals(actual, expected);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test(dataProvider = "dataProviderForAmount", groups = {"green"}, priority = 3, expectedExceptions = {IllegalStateException.class})
    public void testAddTwoNewCurrencySameName(long code, String name, long secondCode){
        AmountInWords.Currency yen = new AmountInWords.Currency((int)code, name) {{
            oneInteger = "иена";
            twoIntegers = "иены";
            fiveIntegers = "иен";
            integerSex = AmountInWords.Sex.FEMALE;
            oneFraction = "сэн";
            twoFractions = "сэн";
            fiveFractions = "сэн";
            fractionSex = AmountInWords.Sex.MALE;
        }};
        AmountInWords.Currency secondYen = new AmountInWords.Currency((int)secondCode, name) {{
            oneInteger = "иена";
            twoIntegers = "иены";
            fiveIntegers = "иен";
            integerSex = AmountInWords.Sex.FEMALE;
            oneFraction = "сэн";
            twoFractions = "сэн";
            fiveFractions = "сэн";
            fractionSex = AmountInWords.Sex.MALE;
        }};
        AmountInWords.addCurrency(yen);
        AmountInWords.addCurrency(secondYen);
        List<Currency> actual = AmountInWords.getCurrencies();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForCurrencies", groups = {"green"}, priority = 1)
    public void testGetDefault(int currencyNum){
        Currency actual = AmountInWords.getDefaultCurrency();
        Currency expected = AmountInWords.getCurrencies().get(currencyNum);
        assertEquals(actual, expected);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForCurrencies", groups = {"green"}, priority = 2)
    public void testSetDefault(int currencyNum){
        AmountInWords.setDefaultCurrency(AmountInWords.getCurrencies().get(currencyNum));
        Currency actual = AmountInWords.getDefaultCurrency();
        Currency expected = AmountInWords.getCurrencies().get(currencyNum);
        assertEquals(actual, expected);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForCurrencies", groups = {"green"}, priority = 4)
    public void testByName(int currencyNum, String name){

        Currency actual = AmountInWords.Currency.byName(name);
        Currency expected = AmountInWords.getCurrencies().get(currencyNum);
        assertEquals(actual, expected);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForCurrencies", groups = {"green"}, priority = 4)
    public void testByNameNegative(int currencyNum, String name){

        Currency actual = AmountInWords.Currency.byName(name);
        Currency expected = null;
        assertEquals(actual, expected);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForCurrencies", groups = {"green"}, priority = 4)
    public void testByCode(int currencyNum, int code){

        Currency actual = AmountInWords.Currency.byCode(code);
        Currency expected = AmountInWords.getCurrencies().get(currencyNum);
        assertEquals(actual, expected);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(dataProvider = "dataProviderForCurrencies", groups = {"green"}, priority = 4)
    public void testByCodeNegative(int currencyNum, int code){

        Currency actual = AmountInWords.Currency.byCode(code);
        Currency expected = null;
        assertEquals(actual, expected);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //@Ignore
    @Test(dataProvider = "dataProviderForAmount", groups = {"red"}, priority = 2)
    public void testFormatUpperBorder(long number, String expected){
        String actual = AmountInWords.format(number);
        assertEquals(actual, expected);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // @Ignore
    @Test(dataProvider = "dataProviderForAmount", groups = {"red"}, priority = 1)
    public void testFormatMinus(long number, String expected){
        String actual = AmountInWords.format(number);
        assertEquals(actual, expected);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
