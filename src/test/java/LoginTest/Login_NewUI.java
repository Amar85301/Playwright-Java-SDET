package LoginTest; // Change this to your package name

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import utils.ExcelUtil;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;

public class Login_NewUI {

	
	Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    Page NewUI;
    FrameLocator frameLeft;
/*
    @BeforeClass
    public void setUp() {
        // Initialize Playwright and launch browser
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }
*/
    
    @BeforeClass // Use this for smaller Laptop To set View port in chromium. Only for OFFICE Laptop
    public void setUp() {
        // Initialize Playwright and launch browser
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setHeadless(false));

        context = browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(1366, 600));  // 🛠️ Added viewport here

        page = context.newPage();
    }

   
    
    @Test(priority = 1)
    public void Verify_Login() {
        page.navigate("https://bytzsoft.in/FlyPalBytz/Login.aspx");
        page.fill("#txtUserName", "Amar");
        page.click("//*[@id=\"lnkArrow\"]/i");
        page.fill("#txtPassword", "6850");
        page.click("//*[@id=\"btnLogin\"]");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);
    }

    @Test(priority = 2)
    public void Verify_clicking_PO_Menu() {
        frameLeft = page.frameLocator("#FrameLeft");
        frameLeft.locator("#menu li:nth-child(7) label").click();
        page.waitForTimeout(5000);
    }

    @Test(priority = 3)
    public void Verify_NewUI_Popup() {
        try {
        	//This locator is stored in a variable named subMenu
            Locator subMenu = frameLeft.locator("#menu > li.Active > div > ul > li:nth-child(1) > a");//Locator is a Playwright object used to find and interact with a web element.
            subMenu.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            NewUI = page.waitForPopup(() -> subMenu.click());//NewUI is the reference to the popup window (created in Test 3).
            NewUI.waitForLoadState();

            String actualURL = NewUI.url();
            System.out.println("New popup opened with URL: " + actualURL);
            Assert.assertTrue(actualURL.contains("https://ui.bytzsoft.in/flypalbytz/order/list/"),
                    "❌ URL mismatch for New UI popup");
        } catch (Exception e) {
            Assert.fail("Exception occurred in Verify_NewUI_Popup: " + e.getMessage());
        }
        }
   
    @Test(priority = 4)
    public void Verify_clicking_CreateOrder() {
    	try {
            // Step 1: Click the Create button
            NewUI.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create")).click();
            System.out.println("✅ Create button clicked.");
            NewUI.waitForTimeout(2000);

            // Step 2: Use getByText for reliability
            Locator heading = NewUI.getByText("Order Params", new Page.GetByTextOptions().setExact(false));

            heading.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(20000)); // Increased timeout

            Assert.assertTrue(heading.isVisible(), "✅ 'Order Params' heading is visible.");

        } catch (Exception e) {
            Assert.fail("❌ Error during Create Order flow: " + e.getMessage());    
        }
    	}

    @Test(priority = 5)
    public void Click_On_Exchange_RadioButton() {
    	 try { //This is a try-catch block used for exception handling.
    		   //If something goes wrong (like element not found or timeout), it won’t crash the test, it will go into the catch block instead.If something goes wrong (like element not found or timeout), it won’t crash the test, it will go into the catch block instead.
    	        
    	        Locator radio = NewUI.getByText("Exchange"); //radio--> variable name given to this element so you can use it later (e.g., for click() or waitFor()).
    	                                                     //This locator is stored in a variable named radio.
    	        radio.waitFor(new Locator.WaitForOptions()
    	                .setState(WaitForSelectorState.VISIBLE)
    	                .setTimeout(10000));

    	        radio.click();
    	        System.out.println("✅ Exchange radio button clicked.");
    	    } catch (Exception e) {
    	        Assert.fail("❌ Failed to click on Exchange radio button: " + e.getMessage());
    	    }
    	 
	        page.waitForTimeout(5000);
    }
    
   
    @Test(priority = 6)
    public void Verify_Clicking_Next_button_On_OrderParameter_Page() {
    	try {
            Locator allNextButtons = NewUI.locator("button:has-text('Next')");
            
            // Filter for the first enabled one
            Locator enabledNextButton = allNextButtons.filter          		
                (new Locator.FilterOptions().setHasNot(NewUI.locator("[disabled]"))).first();

            enabledNextButton.scrollIntoViewIfNeeded();
            enabledNextButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));
            enabledNextButton.click();

            // ✅ ASSERTION: Verify that a known element is visible on the next page
            Locator pageHeader = NewUI.locator("text='Order Details'");
            pageHeader.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));
            Assert.assertTrue(pageHeader.isVisible(), "✅ Navigated to next page.");

            // Debug info
            List<Locator> nextButtons = NewUI.locator("button:has-text('Next')").all();
            System.out.println("🔍 Total 'Next' buttons found: " + nextButtons.size());
            for (int i = 0; i < nextButtons.size(); i++) {
                System.out.println("➡️ Button " + i + ": Disabled = " + nextButtons.get(i).isDisabled());
            }

            System.out.println("✅ Successfully clicked the active 'Next' button--> ORDER PARAMETER PAGE.");
        } catch (Exception e) {
            Assert.fail("❌ Failed during 'Next' button navigation: " + e.getMessage());
        }
    	 page.waitForTimeout(5000);
        }
    
    /*
    @Test (priority = 7) //--> Method for create single Test for every Field.
    public void Verify_Filling_Form_and_Clicking_Next ()
    {
    	 try {
    	        
    		  Locator orderNoInput = NewUI.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Int. Order No"));
    		    
    		    orderNoInput.waitFor(new Locator.WaitForOptions()
    		        .setState(WaitForSelectorState.VISIBLE)
    		        .setTimeout(10000)); // wait up to 10s to be sure it's visible

    		    orderNoInput.fill("ORD12345");
    		    System.out.println("✅ Successfully entered Order No.");

    		} catch (Exception e) {
    		    Assert.fail("❌ Failed to enter Order No: " + e.getMessage());
    }
    	 page.waitForTimeout(5000);

    }
*/
    
    /*
    @Test (priority = 7)
    public void Verify_Filling_OrderDetais ()//--> Method to  fill many fields together
    {
    	 try {
    		NewUI.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Int. Order No"))
    		    .fill("ORD123456666", new Locator.FillOptions().setTimeout(10000));

    		NewUI.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("For Aircraft"))
    		   .fill("Boeing 737", new Locator.FillOptions().setTimeout(10000));

    		//NewUI.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Delivery in Days"))		
    		    //.fill("5", new Locator.FillOptions().setTimeout(10000));
    				
    	NewUI.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Remarks"))
    		  .fill("SDET AMAR", new Locator.FillOptions().setTimeout(10000));

    		System.out.println("✅ All fields filled successfully--> ORDER DETAILS PAGE.");

    		} catch (Exception e) {
    		    Assert.fail("❌ Failed to enter Order Details: " + e.getMessage());
          }
    	 page.waitForTimeout(5000);
          }
   */
    
    @Test (priority= 7)
    public void fillOrderDetailsFromExcel()
    {
    	String path = "D:/Playwright DDT/pworderdetails.xlsx"; // path to your Excel
        String sheet = "Sheet1";
        
        
        //Remember: Row and column indexes start from 0 in Apache POI.
        String intOrderNo = ExcelUtil.getCellValue(path, sheet, 1, 0); // row 1, col 0
        String aircraft   = ExcelUtil.getCellValue(path, sheet, 1, 1); // row 1, col 1
        String remarks    = ExcelUtil.getCellValue(path, sheet, 1, 2); // row 1, col 2

     // Fill each textbox using try-catch to isolate errors
        try {
            NewUI.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Int. Order No")).fill(intOrderNo);
            System.out.println("✅ Int. Order No filled: " + intOrderNo);
        } catch (Exception e) {
            System.out.println("❌ Failed to fill Int. Order No: " + e.getMessage());
        }

        try {
            NewUI.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("For Aircraft")).fill(aircraft);
            System.out.println("✅ Aircraft Name filled: " + aircraft);
        } catch (Exception e) {
            System.out.println("❌ Failed to fill Aircraft Name: " + e.getMessage());
        }

        try {
            NewUI.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Remarks")).fill(remarks);
            System.out.println("✅ Remarks filled: " + remarks);
        } catch (Exception e) {
            System.out.println("❌ Failed to fill Remarks: " + e.getMessage());
        }

        // Proceed to click 'Next' button
        try {
            Locator nextButton = NewUI.locator("button:has-text('Next')")
                                      .filter(new Locator.FilterOptions().setHasNot(NewUI.locator("[disabled]")))
                                      .first();
            nextButton.scrollIntoViewIfNeeded();
            nextButton.click();
            System.out.println("✅ Clicked on Next button.");
        } catch (Exception e) {
            System.out.println("❌ Failed to click Next: " + e.getMessage());
        }
        page.waitForTimeout(5000);
    }
    
    

    @AfterClass
    public void tearDown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }


    }

    

