/*
 * package com.monster.bill.controller;
 * 
 * import java.util.List;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.web.bind.annotation.CrossOrigin; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.PutMapping; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.monster.bill.models.Invoice; import
 * com.monster.bill.models.MakePayment; import com.monster.bill.models.Supplier;
 * import com.monster.bill.service.NetSuiteService;
 * 
 * @RestController
 * 
 * @RequestMapping("/netsuite")
 * 
 * @CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 4800,
 * allowCredentials = "false") public class NetSuiteController {
 * 
 * @Autowired private NetSuiteService netSuiteService;
 * 
 * @PutMapping("/sendSupplier") public Supplier sendSupplier(@RequestParam Long
 * supplierId) {
 * 
 * return netSuiteService.sendSupplier(supplierId); }
 * 
 * @PutMapping("/sendInvoice") public Invoice sendInvoice(@RequestParam Long
 * invoiceId) {
 * 
 * return netSuiteService.sendInvoice(invoiceId); }
 * 
 * @PostMapping("/importPaymentXML") public List<MakePayment> importPaymentXML()
 * { return netSuiteService.importPaymentXML(); }
 * 
 * }
 */