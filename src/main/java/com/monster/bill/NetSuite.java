/*
 * package com.monster.bill;
 * 
 * import java.io.ByteArrayInputStream; import java.io.IOException; import
 * java.net.MalformedURLException; import javax.xml.parsers.DocumentBuilder;
 * import javax.xml.parsers.DocumentBuilderFactory; import
 * javax.xml.parsers.ParserConfigurationException;
 * 
 * import static com.netsuite.webservices.samples.utils.PrintUtils.printError;
 * import org.apache.axis.AxisFault; import org.w3c.dom.Document; import
 * org.w3c.dom.NodeList; import org.xml.sax.SAXException;
 * 
 * import static com.netsuite.webservices.samples.Messages.ERROR_OCCURRED;
 * import static com.netsuite.webservices.samples.Messages.INVALID_WS_URL;
 * import static
 * com.netsuite.webservices.samples.Messages.WRONG_PROPERTIES_FILE;
 * 
 * import com.netsuite.suitetalk.client.v2022_1.WsClient; import
 * com.netsuite.suitetalk.proxy.v2022_1.documents.filecabinet.File; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.core.types.RecordType; import
 * com.netsuite.suitetalk.proxy.v2022_1.platform.messages.ReadResponse; import
 * com.netsuite.webservices.samples.Properties; import
 * com.netsuite.webservices.samples.WsClientFactory;
 * 
 * public class NetSuite {
 * 
 * //@Autowired //private SubsidiaryRepository subsidiaryRepository;
 * 
 * public static void main(String[] args) {
 * 
 * NetSuite netSuite = new NetSuite(); netSuite.importTxnFile();
 * 
 * }
 * 
 * private void importTxnFile() {
 * 
 * WsClient client = null; try { client = WsClientFactory.getWsClient(new
 * Properties(), null); } catch (MalformedURLException e) {
 * printError(INVALID_WS_URL, e.getMessage()); System.exit(2); } catch
 * (AxisFault e) { printError(ERROR_OCCURRED, e.getFaultString());
 * System.exit(3); } catch (IOException e) { printError(WRONG_PROPERTIES_FILE,
 * e.getMessage()); System.exit(1); }
 * 
 * try { ReadResponse response = client.callGetRecord("8937", RecordType.file);
 * File file = (File) response.getRecord(); DocumentBuilderFactory dbf =
 * DocumentBuilderFactory.newInstance(); DocumentBuilder db =
 * dbf.newDocumentBuilder(); Document document = db.parse(new
 * ByteArrayInputStream(file.getContent()));
 * document.getDocumentElement().normalize(); NodeList nList =
 * document.getElementsByTagName("Currency");
 * System.out.println(nList.item(0).getTextContent()); } catch (IOException |
 * ParserConfigurationException | SAXException e) { e.printStackTrace(); }
 * 
 * AnnotationConfigApplicationContext applicationContext = new
 * AnnotationConfigApplicationContext(MonsterBillApplication.class);
 * SubsidiaryRepository subsidiaryRepository =
 * applicationContext.getBean(SubsidiaryRepository.class);
 * System.out.println(subsidiaryRepository.findByIdAndIsDeleted(1L,
 * false).get().getCountry()); applicationContext.close();
 * 
 * 
 * }
 * 
 * }
 */