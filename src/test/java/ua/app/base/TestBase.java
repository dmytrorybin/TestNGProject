package ua.app.base;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import ua.app.utilities.ModifyList;
import ua.app.businessObject.AmountInWords;
import ua.app.utilities.XLSReader;

import static ua.app.businessObject.AmountInWords.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ievgen on 09.05.2016.
 */
public class TestBase extends Assert {

  //  private static String path2;


    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {

    }

    @AfterSuite
    public void afterSuite() {

    }


   @BeforeClass
    public void beforeClass() {

    }

    @AfterClass
    public void AfterClass() {

    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws NoSuchFieldException, IllegalAccessException {
        Field field = AmountInWords.class.getDeclaredField("currencies");
        List<Currency> newList = new ArrayList<Currency>();
        newList.add(UAH); newList.add(EUR); newList.add(USD); newList.add(RUB);
        ModifyList.modify(field, newList);

        field = AmountInWords.class.getDeclaredField("defaultCurrency");
        ModifyList.modify(field, AmountInWords.USD);
//        System.out.println(AmountInWords.getDefaultCurrency());
//        System.out.println(AmountInWords.getCurrencies());
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws NoSuchFieldException, IllegalAccessException {
        Field field = AmountInWords.class.getDeclaredField("currencies");
        List<Currency> newList = new ArrayList<>();
        newList.add(UAH); newList.add(EUR); newList.add(USD); newList.add(RUB);
        ModifyList.modify(field, newList);

        field = AmountInWords.class.getDeclaredField("defaultCurrency");
        ModifyList.modify(field, AmountInWords.UAH);
    }

    @BeforeTest
    public void beforeTest(){}

    @AfterTest
    public void afterTest()  { }

    @Parameters(value = "filePath")
    @DataProvider(name = "dataProviderForAmount")
    public static Object[][] getDataAmount(Method method) {

     //   path2 = filePath;
        String nameSheet = method.getName().toString();
        XLSReader reader = new XLSReader("src\\test\\resources\\testData.xls");

        return reader.parseAmount(nameSheet);
    }

    @DataProvider(name = "dataProviderForCurrencies")
    public static Object[][] getDataCurr(Method method) {

        //    path2 = filePath;
        String nameSheet = method.getName().toString();
        XLSReader reader = new XLSReader("src\\test\\resources\\testData.xls");

        return reader.parseCurrency(nameSheet);
    }

    protected void print(String msg) throws NumberFormatException{
        System.out.println(msg);
    }
}
