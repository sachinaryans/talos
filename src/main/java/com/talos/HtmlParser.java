package com.talos;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.talos.constants.StringConstants;
import com.talos.excel.CreateExcelTemplate;
import com.talos.pojo.ComponentDetails;
import com.talos.pojo.ComponentDetails.AttributeDetail;
import com.talos.pojo.ComponentDetails.Component;
import com.talos.pojo.ComponentDetails.ElementDetail;
import com.talos.selenium.utils.CommonUtils;

/**
 * The Class HtmlParser.
 * @author Sachin
 */
public class HtmlParser extends Init implements StringConstants {
	
	/** The Constant logger. */
	final static Logger logger = Logger.getLogger(HtmlParser.class);
	
	/** The component details. */
	ComponentDetails componentDetails;
	
	/** The workbook. */
	Workbook workbook = null;
	
	/** The cell ctr. */
	int cellCtr = 4;
	
	/** The parser driver. */
	WebDriver parserDriver;

	/**
	 * This method reads the html and creates the template
	 *
	 * @param generatorDriver the generator driver
	 */
	public void parse(WebDriver generatorDriver) {
		try {
			cellCtr = 4;
			if (generatorDriver != null) {
				parserDriver = generatorDriver;
				loadComponentDetails();
				workbook = CreateExcelTemplate.createHeaders();
				Document doc = Jsoup.parse(generatorDriver.getPageSource());
				printAllElement(doc);
				String fileName = doc.title()
						.replace(SPACE, BLANK)
						.replace(HYPHEN, BLANK)
						.replace(DOT, BLANK)
						.concat(".xlsx");
				CreateExcelTemplate.writeEndFieldDetails(workbook.getSheetAt(0), cellCtr + 1);
				CreateExcelTemplate.saveExcel(workbook, templateDir.concat(File.separator).concat(fileName));
			} else {
				logger.error("Browser not launched, please navigate to the url");
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Prints the all element.
	 *
	 * @param element the element
	 */
	public void printAllElement(Element element) {
		List<Element> childElementList = element.children();
		for (Element el : childElementList) {
			findComponent(el);
			if (el.childNodeSize() > 0) {
				printAllElement(el);
			}
		}
	}

	/**
	 * Load component details.
	 */
	public void loadComponentDetails() {
		try {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			componentDetails = mapper.readValue(new File("conf/componentDetails.yml"), ComponentDetails.class);

		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Find component.
	 *
	 * @param el the el
	 */
	public void findComponent(Element el) {
		String componentName = BLANK;
		boolean foundComponent = false;
		try {
			for (Component component : componentDetails.getComponentDetails()) {
				if (el.tagName().equalsIgnoreCase(component.getTag())
						&& el.className().equalsIgnoreCase(component.getClassName())) {
					componentName = component.getComponent();
					foundComponent = true;
					if (component.getComponentAttributeDetails() != null) {
						for (AttributeDetail attributeDetail : component.getComponentAttributeDetails()) {
							if (el.hasAttr(attributeDetail.getAttributeName())
									&& el.attr(attributeDetail.getAttributeName()).equalsIgnoreCase(
											attributeDetail.getValue())
									&& attributeDetail.isCheck()) {
								foundComponent = true;
							} else if (el.hasAttr(attributeDetail.getAttributeName())
									&& !el.attr(attributeDetail.getAttributeName())
											.equalsIgnoreCase(attributeDetail.getValue())
									&& !attributeDetail.isCheck()) {
								foundComponent = true;
							} else {
								foundComponent = false;
							}
						}
					}
				}
				if (component.getElementDetails() != null) {
					for (ElementDetail elementDetail : component.getElementDetails()) {
						if (el.childNodeSize() > 1) {
							List<Element> childElementList = el.children();
							innerLoop: for (Element el1 : childElementList) {
								boolean found = findChildElementDetail(el1, elementDetail);
								foundComponent = !found ? found : foundComponent;
								break innerLoop;
							}
						}
					}
				}
				if (foundComponent) {
					break;
				}
			}
			if (foundComponent) {
				printComponentDetails(el, componentName);
			}
		} catch (Exception e) {
			logger.error(e);
		}

	}

	/**
	 * Prints the component details.
	 *
	 * @param el the el
	 * @param component the component
	 */
	public void printComponentDetails(Element el, String component) {
		try {
			cellCtr++;
			String xpath = getXpath(el);
			String label = getLabel(el, component);
			CreateExcelTemplate.writeFieldDetails(workbook.getSheetAt(0), cellCtr, component, xpath, label);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Find child element detail.
	 *
	 * @param el the el
	 * @param elementDetail the element detail
	 * @return true, if successful
	 */
	public boolean findChildElementDetail(Element el, ElementDetail elementDetail) {
		boolean found = false;
		if (el.tagName().equalsIgnoreCase(elementDetail.getTag())
				&& el.className().equalsIgnoreCase(elementDetail.getClassName())) {
			found = true;
			if (elementDetail.getElementAttributeDetails() != null) {
				for (AttributeDetail attributeDetail : elementDetail.getElementAttributeDetails()) {
					if (el.hasAttr(attributeDetail.getAttributeName())
							&& el.attr(attributeDetail.getAttributeName()).equalsIgnoreCase(attributeDetail.getValue())
							&& attributeDetail.isCheck()) {
						found = true;
					} else if (el.hasAttr(attributeDetail.getAttributeName())
							&& !el.attr(attributeDetail.getAttributeName()).equalsIgnoreCase(attributeDetail.getValue())
							&& !attributeDetail.isCheck()) {
						found = true;
					} else {
						found = false;
					}
				}
			}
		}
		for (Element el1 : el.children()) {

			found = findChildElementDetail(el1, elementDetail);
		}
		return found;
	}

	/**
	 * Gets the xpath.
	 *
	 * @param el the el
	 * @return the xpath
	 */
	public String getXpath(Element el) {
		StringBuilder absPath = new StringBuilder();
		String xpath = BLANK;
		int elCtr = 1;
		absPath.append(SLASH);
		Elements parents = el.parents();
		for (int i = parents.size() - 1; i >= 0; i--) {
			elCtr = 1;
			Element element = parents.get(i);
			absPath.append(SLASH);
			absPath.append(element.tagName());
			if (!element.tagName().equals("html") && !element.tagName().equals("body")
					&& !element.tagName().equals("main")) {
				elCtr = getElementIndex(element);
				absPath.append("[");
				absPath.append(elCtr);
				absPath.append("]");
			}
		}
		absPath.append(SLASH).append(el.tagName());
		elCtr = getElementIndex(el);
		absPath.append("[");
		absPath.append(elCtr);
		absPath.append("]");
		xpath = absPath.toString();
		return xpath;
	}

	/**
	 * Gets the element index.
	 *
	 * @param element the element
	 * @return the element index
	 */
	public int getElementIndex(Element element) {
		int ctr = 1;
		String tag = element.tagName();
		for (Element el : element.parent().children()) {
			if (element == el) {
				break;
			} else if (el.tagName().equals(tag) && element != el) {
				ctr++;
			}
		}
		return ctr;
	}

	/**
	 * Gets the label.
	 *
	 * @param el the el
	 * @param component the component
	 * @return the label
	 */
	public String getLabel(Element el, String component) {
		String label = BLANK;
		try {

			String forName = el.id();
			WebElement listelFor = CommonUtils.getWebElement(parserDriver, "//*[@for='" + forName + "']");
			if (listelFor != null) {
				label = listelFor.getAttribute("innerText");
			} else {
				label = el.wholeText().trim();
			}
			if (label.isEmpty()) {
				label = el.attr("value");
			}
			label = label.isEmpty() ? component : label;
		} catch (Exception e) {
			logger.error(e);
		}
		return label;

	}
}
