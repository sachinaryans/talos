package com.talos.pojo;

import java.util.List;

/**
 * The Class ComponentDetails.
 * @author Sachin
 */
public class ComponentDetails {
	
	/** The component details. */
	List<Component> componentDetails;

	/**
	 * Gets the component details.
	 *
	 * @return the component details
	 */
	public List<Component> getComponentDetails() {
		return componentDetails;
	}

	/**
	 * Sets the component details.
	 *
	 * @param componentDetails the new component details
	 */
	public void setComponentDetails(List<Component> componentDetails) {
		this.componentDetails = componentDetails;
	}

	/**
	 * The Class Component.
	 */
	public static class Component {
		
		/** The component. */
		String component;
		
		/** The tag. */
		String tag;
		
		/** The method. */
		String method;

		/**
		 * Gets the component.
		 *
		 * @return the component
		 */
		public String getComponent() {
			return component;
		}

		/**
		 * Sets the component.
		 *
		 * @param component the new component
		 */
		public void setComponent(String component) {
			this.component = component;
		}

		/**
		 * Gets the tag.
		 *
		 * @return the tag
		 */
		public String getTag() {
			return tag;
		}

		/**
		 * Sets the tag.
		 *
		 * @param tag the new tag
		 */
		public void setTag(String tag) {
			this.tag = tag;
		}

		/**
		 * Gets the class name.
		 *
		 * @return the class name
		 */
		public String getClassName() {
			return className;
		}

		/**
		 * Sets the class name.
		 *
		 * @param className the new class name
		 */
		public void setClassName(String className) {
			this.className = className;
		}

		/**
		 * Gets the component attribute details.
		 *
		 * @return the component attribute details
		 */
		public List<AttributeDetail> getComponentAttributeDetails() {
			return componentAttributeDetails;
		}

		/**
		 * Sets the component attribute details.
		 *
		 * @param componentAttributeDetails the new component attribute details
		 */
		public void setComponentAttributeDetails(List<AttributeDetail> componentAttributeDetails) {
			this.componentAttributeDetails = componentAttributeDetails;
		}

		/**
		 * Gets the element details.
		 *
		 * @return the element details
		 */
		public List<ElementDetail> getElementDetails() {
			return elementDetails;
		}

		/**
		 * Sets the element details.
		 *
		 * @param elementDetails the new element details
		 */
		public void setElementDetails(List<ElementDetail> elementDetails) {
			this.elementDetails = elementDetails;
		}

		/** The class name. */
		String className;
		
		/** The component attribute details. */
		List<AttributeDetail> componentAttributeDetails;
		
		/** The element details. */
		List<ElementDetail> elementDetails;

		/**
		 * Gets the method.
		 *
		 * @return the method
		 */
		public String getMethod() {
			return method;
		}

		/**
		 * Sets the method.
		 *
		 * @param method the new method
		 */
		public void setMethod(String method) {
			this.method = method;
		}
	}

	/**
	 * The Class ElementDetail.
	 */
	public static class ElementDetail {
		
		/** The tag. */
		String tag;
		
		/** The class name. */
		String className;
		
		/** The check. */
		boolean check;

		/**
		 * Gets the tag.
		 *
		 * @return the tag
		 */
		public String getTag() {
			return tag;
		}

		/**
		 * Sets the tag.
		 *
		 * @param tag the new tag
		 */
		public void setTag(String tag) {
			this.tag = tag;
		}

		/**
		 * Gets the class name.
		 *
		 * @return the class name
		 */
		public String getClassName() {
			return className;
		}

		/**
		 * Sets the class name.
		 *
		 * @param className the new class name
		 */
		public void setClassName(String className) {
			this.className = className;
		}

		/**
		 * Checks if is check.
		 *
		 * @return true, if is check
		 */
		public boolean isCheck() {
			return check;
		}

		/**
		 * Sets the check.
		 *
		 * @param check the new check
		 */
		public void setCheck(boolean check) {
			this.check = check;
		}

		/**
		 * Gets the element attribute details.
		 *
		 * @return the element attribute details
		 */
		public List<AttributeDetail> getElementAttributeDetails() {
			return elementAttributeDetails;
		}

		/**
		 * Sets the element attribute details.
		 *
		 * @param elementAttributeDetails the new element attribute details
		 */
		public void setElementAttributeDetails(List<AttributeDetail> elementAttributeDetails) {
			this.elementAttributeDetails = elementAttributeDetails;
		}

		/** The element attribute details. */
		List<AttributeDetail> elementAttributeDetails;
	}

	/**
	 * The Class AttributeDetail.
	 */
	public static class AttributeDetail {
		
		/** The attribute name. */
		String attributeName;
		
		/** The value. */
		String value;

		/**
		 * Gets the attribute name.
		 *
		 * @return the attribute name
		 */
		public String getAttributeName() {
			return attributeName;
		}

		/**
		 * Sets the attribute name.
		 *
		 * @param attributeName the new attribute name
		 */
		public void setAttributeName(String attributeName) {
			this.attributeName = attributeName;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Sets the value.
		 *
		 * @param value the new value
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * Checks if is check.
		 *
		 * @return true, if is check
		 */
		public boolean isCheck() {
			return check;
		}

		/**
		 * Sets the check.
		 *
		 * @param check the new check
		 */
		public void setCheck(boolean check) {
			this.check = check;
		}

		/** The check. */
		boolean check;
	}
}
