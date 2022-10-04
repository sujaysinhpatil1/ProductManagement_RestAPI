package com.jbk.product.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jbk.product.dao.ProductDao;
import com.jbk.product.entity.Product;
import com.jbk.product.sort.ProductIDComparator;
import com.jbk.product.sort.ProductNameComparator;

@Component
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao dao;

	@Override
	public boolean saveProduct(Product product) {

		if (product.getProductId() == null) {
			String id = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
			product.setProductId(id);
		}
		boolean isAdded = dao.saveProduct(product);
		return isAdded;
	}

	@Override
	public List<Product> getAllProduct() {
		List<Product> allProduct = dao.getAllProduct();
		return allProduct;
	}

	@Override
	public Product getProductById(String productId) {
		Product product = dao.getProductById(productId);
		return product;
	}

	@Override
	public boolean deleteProduct(String productId) {
		boolean isDeleted = dao.deleteProduct(productId);
		return isDeleted;
	}

	@Override
	public boolean updateProduct(Product product) {

		boolean isUpdated = dao.updateProduct(product);
		return isUpdated;
	}

	@Override
	public List<Product> sortProduct(String sortBy) {
		List<Product> list = getAllProduct();

		if (list.size() > 1) {
			if (sortBy.equalsIgnoreCase("productName")) {
				Collections.sort(list, new ProductNameComparator());
			} else if (sortBy.equalsIgnoreCase("productId")) {
				Collections.sort(list, new ProductIDComparator().reversed());
				// Collections.reverse(list);
			}
		}
		return list;
	}

	@Override
	public double getTotalCount() { // by using core java >> Projections.rowCount();
		List<Product> list = getAllProduct();
		double count = list.stream().count();
		return count;
	}

	@Override
	public Product getMaxPriceProduct() { // by using core java >> first get max (Projections.max("productPrice")
		List<Product> list = getAllProduct(); // >> second Restrictions.eq("productPrice", maxValue)
		Product product = null;

		// Collections.sort(list, new ProducPriceComparator());
		// Product product = list.get(0);
		if (!list.isEmpty()) {
			product = list.stream().max(Comparator.comparingDouble(Product::getProductPrice)).get();
		}
		return product;

	}

	@Override
	public double countSumProductPrice() { // by using core java >> Projection.sum("productPrice");
		double sum = 0;
		List<Product> list = getAllProduct();
		if (!list.isEmpty()) {
			sum = list.stream().mapToDouble(Product::getProductPrice).sum();
		}
		return sum;

	}

	public List<Product> readExcel(String filePath) {

		FileInputStream fis = null;
		List<Product> list = new ArrayList<>();
		Product product = null;

		try {
			fis = new FileInputStream(new File(filePath));
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rows = sheet.rowIterator();
			
			int count = 0;
			while (rows.hasNext()) {
				Row row = rows.next();
				product = new Product();
				//Thread.sleep(1);
				String id = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
				product.setProductId(id + count++);
				Iterator<Cell> cells = row.cellIterator();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					int column = cell.getColumnIndex();

					switch (column) {
					case 0: {
						product.setProductName(cell.getStringCellValue());
						break;
					}
					case 1: {
						product.setProductQty((int) cell.getNumericCellValue());
						break;
					}
					case 2: {
						product.setProductPrice(cell.getNumericCellValue());
						break;
					}
					case 3: {
						product.setProductType(cell.getStringCellValue());
						break;
					}
					
					}
					
				}
				list.add(product);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	@Override
	public int uploadSheet(CommonsMultipartFile file, HttpSession httpSession) {

		String path = httpSession.getServletContext().getRealPath("/"); // root
		String filename = file.getOriginalFilename();
		int count = 0;
		FileOutputStream fos = null;
		byte[] data = file.getBytes(); // read data
		try {
			System.out.println(path);
			fos = new FileOutputStream(new File(path + File.separator + filename));
			fos.write(data); // write data

			List<Product> list = readExcel(path + File.separator + filename);
			
			count = dao.uploadProductList(list);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	@Override
	public String exportToExcel() {
		List<Product> allProduct = getAllProduct();
		String[] columns = { "ID", "NAME", "QTY", "PRICE", "TYPE" };
		try {

			// Create a Workbook
			Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

			/*
			 * CreationHelper helps us create instances of various things like DataFormat,
			 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
			 */
			CreationHelper createHelper = workbook.getCreationHelper();

			// Create a Sheet
			Sheet sheet = workbook.createSheet("product");

			// Create a Font for styling header cells
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.RED.getIndex());

			// Create a CellStyle with the font
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Create a Row
			Row headerRow = sheet.createRow(0);

			// Create cells
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}

			// Create Other rows and cells with employees data
			int rowNum = 1;
			for (Product product : allProduct) {
				Row row = sheet.createRow(rowNum++);

				row.createCell(0).setCellValue(product.getProductId());

				row.createCell(1).setCellValue(product.getProductName());

				row.createCell(2).setCellValue(product.getProductQty());

				row.createCell(3).setCellValue(product.getProductPrice());

				row.createCell(4).setCellValue(product.getProductType());
			}

			// Resize all columns to fit the content size
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// Write the output to a file
			String path = httpSession.getServletContext().getRealPath("/exported"); // root
			String filename = file.getOriginalFilename();
			FileOutputStream fileOut = new FileOutputStream(path + File.separator + filename);
			workbook.write(fileOut);
			fileOut.close();

			// Closing the workbook
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Exported at"+ path;
	
	}

}
