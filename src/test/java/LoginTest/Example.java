package LoginTest;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

public class Example {

	public static void main(String[] args) {
		try (Playwright playwright = Playwright.create()) {
		      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
		        .setHeadless(false));
		      BrowserContext context = browser.newContext();
		      Page page = context.newPage();
		      page.navigate("https://bytzsoft.in/FlyPalBytz/Login.aspx");
		      page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("User Name")).click();
		      page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("User Name")).fill("amar");
		      page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("User Name")).press("Enter");
		      page.getByPlaceholder("Password", new Page.GetByPlaceholderOptions().setExact(true)).click();
		      page.getByPlaceholder("Password", new Page.GetByPlaceholderOptions().setExact(true)).fill("6850");
		      page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("")).click();
		      page.locator("#FrameLeft").contentFrame().getByText("✈ Purchase Orders").click();
		      Page page1 = page.waitForPopup(() -> {
		        page.locator("#FrameLeft").contentFrame().getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("▶ Outright/EX/OH/RE")).click();
		      });
		      page1.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create")).click();
		      page1.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Create Order")).click();
		      page1.getByLabel("Exchange").check();
		    }
		  }

	}


