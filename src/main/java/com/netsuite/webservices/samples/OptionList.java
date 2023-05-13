/*
 * package com.netsuite.webservices.samples;
 * 
 * import java.util.LinkedHashMap; import java.util.Map;
 * 
 * import static
 * com.netsuite.suitetalk.client.common.utils.CommonUtils.isEmptyString; import
 * static com.netsuite.webservices.samples.Messages.SELECTION; import static
 * com.netsuite.webservices.samples.io.Console.readLine; import static
 * com.netsuite.webservices.samples.utils.IndentationUtils.getNumberPlaces;
 * import static com.netsuite.webservices.samples.utils.PrintUtils.print; import
 * static com.netsuite.webservices.samples.utils.PrintUtils.printEmptyLine;
 * import static
 * com.netsuite.webservices.samples.utils.PrintUtils.printInvalidChoiceMessage;
 * 
 *//**
	 * <p>
	 * This class represents a list of options. A user can choose from this options
	 * or quit.
	 * </p>
	 * <p>
	 * © 2019 NetSuite Inc. All rights reserved.
	 * </p>
	 */
/*
 * public class OptionList<T> {
 * 
 * private String listLabel; private Map<String, T> options = new
 * LinkedHashMap<>();
 * 
 * private String quitOption; private String quitLabel;
 * 
 *//**
	 * Initializes list of options.
	 *
	 * @param listLabel The first line of text usually containing prompt for the
	 *                  user or some information about the following options
	 * @param options   Options to be listed. It is a map where key is the text
	 *                  displayed to user and the value is the object which will be
	 *                  returned if a user selects that particular option.
	 */
/*
 * public OptionList(String listLabel, Map<String, T> options) { this.listLabel
 * = listLabel; this.options = options; }
 * 
 * public String getListLabel() { return listLabel; }
 * 
 * public void setListLabel(String listLabel) { this.listLabel = listLabel; }
 * 
 * public Map<String, T> getOptions() { return options; }
 * 
 * public void setOptions(Map<String, T> options) { this.options = options; }
 * 
 * public void setQuitOption(char option, String label) { quitOption =
 * String.valueOf(option); // We want to have a quit option with one single
 * character quitLabel = label; }
 * 
 *//**
	 * Displays the list to the user and asks him/her to enter his/her choice.
	 */
/*
 * private void display() { if (!isEmptyString(listLabel)) { print(listLabel); }
 * int i = 0; for (String option : options.keySet()) { print("%1$" +
 * getNumberPlaces() + "d) %2$s", ++i, option); } if
 * (!isEmptyString(quitOption)) { print("%1$" + getNumberPlaces() + "s) %2$s",
 * quitOption, quitLabel); } }
 * 
 *//**
	 * @return Selected operation or {@code null} if quit option was selected.
	 */
/*
 * public T displayAndGetUserChoice() { while (true) { printEmptyLine();
 * display(); printEmptyLine(); String selectedIndex =
 * readLine(SELECTION).trim(); T selectedOption = null; if
 * (selectedIndex.matches("\\d+")) { selectedOption =
 * getOption(Integer.parseInt(selectedIndex)); } else if (quitOption != null &&
 * quitOption.equalsIgnoreCase(selectedIndex)) { return null; // User wants to
 * quit this list } if (selectedOption == null) { printInvalidChoiceMessage(); }
 * else { return selectedOption; } } }
 * 
 *//**
	 * @param index Index of selected option
	 * @return Object stored as a value for an option with given {@code index}
	 *//*
		 * private T getOption(int index) { int i = 0; for (T option : options.values())
		 * { if (++i == index) { return option; } } return null; } }
		 */