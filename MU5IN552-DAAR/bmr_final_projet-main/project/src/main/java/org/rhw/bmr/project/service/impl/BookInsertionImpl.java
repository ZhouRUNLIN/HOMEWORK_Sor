package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.*;
import org.redisson.api.RBloomFilter;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.service.BookInsertionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class BookInsertionImpl extends ServiceImpl<BookMapper, BookDO> implements BookInsertionService {

    private static final Logger logger = LoggerFactory.getLogger(BookInsertionImpl.class);

    @Value("${scheduledTaskInsertBook.url}")
    private String url;

    @Value("${scheduledTaskInsertBook.backingUp}")
    private String backupFolder;

    @Value("${scheduledTaskInsertBook.formworkFile}")
    private String formworkFile;

    @Autowired
    private RBloomFilter<String> booksInsertBloomFilter;

    @Override
    public void insertBook() {
        logger.info("Start processing folder: {}", url);
        Path folderPath = Paths.get(url);

        if (!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            logger.error("The specified path does not exist or is not a folder: {}", url);
            return;
        }

        Path backupPath = Paths.get(backupFolder);
        try {
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
                logger.info("Create backup folder: {}", backupFolder);
            }
        } catch (IOException e) {
            logger.error("Cannot create backup folder {}: {}", backupFolder, e.getMessage(), e);
            return;
        }

        File folder = folderPath.toFile();
        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".csv") ||
                        name.toLowerCase().endsWith(".xlsx") ||
                        name.toLowerCase().endsWith(".xls")
        );

        if (files == null || files.length == 0) {
            logger.info("There are no CSV or Excel files in the folder that can be processed.。");

            Path formworkPath = Paths.get(formworkFile);
            File formwork = formworkPath.toFile();

            if (!formwork.exists()) {
                createFormworkFile();
            } else {
                logger.info("The form template file already exists.: {}", formworkFile);
            }

            logger.info("The task is complete and no further action is required.");
            return;
        }

        for (File file : files) {
            logger.info("Processing document: {}", file.getName());
            try {
                List<BookDO> books = new ArrayList<>();

                if (file.getName().toLowerCase().endsWith(".csv")) {
                    books = parseCSV(file);
                } else if (file.getName().toLowerCase().endsWith(".xlsx") || file.getName().toLowerCase().endsWith(".xls")) {
                    books = parseExcel(file);
                }

                if (!books.isEmpty()) {
                    saveBatch(books);
                    logger.info("Successfully inserted {} records into the database.。", books.size());

                    backupFile(file, backupPath);
                } else {
                    logger.warn("There is no valid data in the file {}。", file.getName());
                    backupFile(file, backupPath);
                }
            } catch (Exception e) {
                logger.error("An error occurred while processing the file {}: {}", file.getName(), e.getMessage(), e);
            }
        }

        // createFormworkFile();

        logger.info("Finished with the folder: {}", url);
    }

    private List<BookDO> parseCSV(File file) throws IOException, CsvValidationException {
        List<BookDO> books = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] headers = csvReader.readNext(); // 读取标题行
            if (headers == null || headers.length < 9) {
                logger.warn("CSV file {} does not have enough header columns.", file.getName());
                return books;
            }

            String[] values;
            while ((values = csvReader.readNext()) != null) {
                if (values.length < 9) {
                    logger.warn("The CSV file {} has insufficient number of columns per row: {}", file.getName(), String.join(",", values));
                    continue;
                }

                BookDO book = mapToBookDO(values);
                if (book != null) {

                    if (!booksInsertBloomFilter.contains(String.valueOf(book.getRefId()))){
                        books.add(book);
                        booksInsertBloomFilter.add(String.valueOf(book.getRefId()));
                    }


                }
            }
        }

        return books;
    }

    private List<BookDO> parseExcel(File file) throws IOException {
        List<BookDO> books = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) {
                logger.warn("Excel file {} No data.", file.getName());
                return books;
            }

            Row headerRow = rowIterator.next();
            if (headerRow.getLastCellNum() < 9) {
                logger.warn("The Excel file {} has an insufficient number of header rows.", file.getName());
                return books;
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String[] values = new String[9];
                for (int i = 0; i < 9; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    values[i] = getCellValueAsString(cell);
                }

                BookDO book = mapToBookDO(values);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while parsing the Excel file {}: {}", file.getName(), e.getMessage(), e);
            throw e;
        }

        return books;
    }

    private BookDO mapToBookDO(String[] values) {
        try {
            BookDO book = new BookDO();

            book.setRefId(StringUtils.hasText(values[0]) ? Long.valueOf(values[0].trim()) : null);
            book.setTitle(StringUtils.hasText(values[1]) ? values[1].trim() : null);
            book.setStoragePath(StringUtils.hasText(values[2]) ? values[2].trim() : null);
            book.setAuthor(StringUtils.hasText(values[3]) ? values[3].trim() : null);
            book.setCategory(StringUtils.hasText(values[4]) ? values[4].trim() : null);
            book.setDescription(StringUtils.hasText(values[5]) ? values[5].trim() : null);
            book.setLanguage(StringUtils.hasText(values[6]) ? values[6].trim() : null);

            if (StringUtils.hasText(values[7])) {
                try {
                    book.setClickCount((long) Integer.parseInt(values[7].trim()));
                } catch (NumberFormatException e) {
                    logger.warn("Cannot resolve clickCount: {}, default is 0", values[7]);
                    book.setClickCount(0L);
                }
            } else {
                book.setClickCount(0L);
            }

            book.setImg(StringUtils.hasText(values[8]) ? values[8].trim() : null);
            book.setEsSyncFlag(0);

            return book;
        } catch (Exception e) {
            logger.error("Error mapping BookDO: {}", e.getMessage(), e);
            return null;
        }
    }


    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();

                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    switch (cellValue.getCellType()) {
                        case STRING:
                            return cellValue.getStringValue();
                        case NUMERIC:
                            return String.valueOf(cellValue.getNumberValue());
                        case BOOLEAN:
                            return String.valueOf(cellValue.getBooleanValue());
                        default:
                            return "";
                    }
                } catch (Exception e) {
                    logger.warn("Error parsing formula cell: {}", e.getMessage(), e);
                    return "";
                }
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return "";
        }
    }


    private void backupFile(File file, Path backupPath) {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String dateTimeStr = currentDateTime.format(formatter);

            String newFileName = "InfoBackingUp_" + dateTimeStr + "_" + file.getName();

            Path targetPath = backupPath.resolve(newFileName);

            Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            logger.info("Files backed up and renamed: {} to {}", file.getName(), targetPath.toString());
        } catch (IOException e) {
            logger.error("An error occurred while backing up the file {}: {}", file.getName(), e.getMessage(), e);
        }
    }



    private void createFormworkFile() {
        Path formworkPath = Paths.get(formworkFile);
        File formwork = formworkPath.toFile();

        if (formwork.exists()) {
            logger.info("The form template file already exists: {}", formworkFile);
            return;
        }

        String[] headers = {"refId", "title", "storagePath", "author", "category", "description", "language", "clickCount", "img"};

        try (java.io.FileWriter writer = new java.io.FileWriter(formwork)) {
            writer.append(String.join(",", headers)).append("\n");
            logger.info("Table template file created: {}", formworkFile);
        } catch (IOException e) {
            logger.error("An error occurred while creating the table template file {}: {}", formworkFile, e.getMessage(), e);
        }
    }

}
