package com.example.library_three;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class LibraryApp extends Application {
    private Library library = new Library(10);
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Библиотека");


        // Основная таблица (Все книги)
        TableView<Book> bookTable = new TableView<>();
        ObservableList<Book> books = FXCollections.observableArrayList(library.getAllBooks());
        bookTable.setItems(books);

        //Добавление книг по умолчанию
        library.addBook("Война и мир", "Толстой Лев Николаевич");
        library.addBook("Преступление и наказание", "Фёдор Михайлович Достоевский");
        library.addBook("Мастер и Маргарита", "Михаил Афанасьевич Булгаков");
        library.addBook("Атлант расправил плечи", "Айн Рэнд");

        //Добавление читателей по умолчанию
        library.getBookByTitle("Война и мир").addReaderRecord("Иванов", 5);
        library.getBookByTitle("Война и мир").addReaderRecord("Петров", 10);
        library.getBookByTitle("Война и мир").addReaderRecord("Сидоров", 1);
        library.getBookByTitle("Преступление и наказание").addReaderRecord("Иванова", 1);
        library.getBookByTitle("Преступление и наказание").addReaderRecord("Петрова", 12);
        library.getBookByTitle("Преступление и наказание").addReaderRecord("Сидорова", 33);

        library.getBookByTitle("Мастер и Маргарита").addReaderRecord("Иванова", 4);
        library.getBookByTitle("Мастер и Маргарита").addReaderRecord("Петрова", 8);
        library.getBookByTitle("Мастер и Маргарита").addReaderRecord("Сидорова", 11);

        library.getBookByTitle("Атлант расправил плечи").addReaderRecord("Афанасьев", 51);
        library.getBookByTitle("Атлант расправил плечи").addReaderRecord("Дмитриев", 120);
        library.getBookByTitle("Атлант расправил плечи").addReaderRecord("Колобков", 3);



        // Колонки для основной таблицы
        TableColumn<Book, String> titleColumn = new TableColumn<>("Название книги");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Автор");
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));

        TableColumn<Book, Void> buttonColumn = new TableColumn<>("Действия");
        //Логика кнопки открытия меню книги
        buttonColumn.setCellFactory(col -> new TableCell<>() {
            private final Button openButton = new Button("Открыть");
            {
                openButton.setOnAction(e -> {
                    Book selectedBook = getTableView().getItems().get(getIndex());
                    if (selectedBook != null) {
                        // Вызов метода для отображения меню книги
                        showBookDetails(selectedBook);
                    }
                });

            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(openButton);
                }
            }
        });

        //Добавление в таблицу элементов, колонок
        bookTable.getColumns().addAll(titleColumn, authorColumn, buttonColumn);

//        bookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                showBookDetails(newValue);
//            }
//        });

        // Кнопка добавления книги
        Button addBookButton = new Button("Добавить книгу");
        addBookButton.setOnAction(e -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("Введите название книги:");
            //Отображение поля для ввода названия книги
            inputDialog.showAndWait().ifPresent(title -> {
                TextInputDialog authorDialog = new TextInputDialog();
                authorDialog.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
                    if (!newValue.matches("[а-яА-Я -]*")) {
                        authorDialog.getEditor().setText(newValue.replaceAll("[^а-яА-Я -]", ""));
                    };
                });
                authorDialog.setHeaderText("Введите автора книги:");
                //Отображение поля для ввода автора книги
                authorDialog.showAndWait().ifPresent(author -> {
                    library.addBook(title, author);
                    books.setAll(library.getAllBooks());
                });
            });
        });
        //кнопка удаления книги из очереди
        Button removeBookButton = new Button("Удалить книгу");
        removeBookButton.setOnAction(e -> {
            library.removeBook();
            books.setAll(library.getAllBooks());
        });

        HBox buttonBox = new HBox(10, addBookButton, removeBookButton);



        //отображение стандартных книг в таблице
        VBox vbox = new VBox(10, bookTable, buttonBox);
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        books.setAll(library.getAllBooks());

    }

    private void showBookDetails(Book book) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Детали книги");

        TableView<ReaderRecord> readerTable = new TableView<>();
        //для листа, collections
        //ObservableList<ReaderRecord> readers = FXCollections.observableArrayList(book.getReaderRecords());
        ObservableList<ReaderRecord> readers = FXCollections.observableArrayList();
        for (ReaderRecord record : book.getReaderRecords()) {
            readers.add(record);
        }
        readerTable.setItems(readers);

        TableColumn<ReaderRecord, String> readerNameColumn = new TableColumn<>("Читатель");
        readerNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReaderName()));

        TableColumn<ReaderRecord, Integer> daysColumn = new TableColumn<>("Дни использования");
        daysColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getDays()).asObject());

        TableColumn<ReaderRecord, Void> buttonColumn = new TableColumn<>("Действия");
        //Отслежтивание нажатия кнопки для получения информации о читателях, логика создания окна
        buttonColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Удалить");

            {
                //Обработчик удаления читателя
                deleteButton.setOnAction(e -> {
                    ReaderRecord readerRecord = getTableView().getItems().get(getIndex());
                    if (readerRecord != null) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Удаление читателя");
                        alert.setHeaderText("Вы точно хотите удалить читателя " + readerRecord.getReaderName() + " из книги " + book.getTitle());
                        Optional<ButtonType> option = alert.showAndWait();
                        if (option.isPresent() && option.get() == ButtonType.OK) {
                            book.removeReaderRecord(readerRecord.getReaderName());
                            readers.remove(readerRecord);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

// Добавление колонки читателей в таблицу
        readerTable.getColumns().addAll(readerNameColumn, daysColumn, buttonColumn);

        HBox buttonBox = new HBox(10);
        Button addReaderButton = new Button("Добавить читателя");
        //Логика кнопки добавления читателя, вызов функции начала отображения диалоговых окон для ввода данных
        addReaderButton.setOnAction(e -> {
            TextInputDialog readerDialog = new TextInputDialog();
            readerDialog.setHeaderText("Введите имя читателя:");
            readerDialog.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
                if (!newValue.matches("[а-яА-Я -]*")) {
                    readerDialog.getEditor().setText(newValue.replaceAll("[^а-яА-Я -]", ""));
                }
            });
            readerDialog.showAndWait().ifPresent(readerName -> {
                TextInputDialog daysDialog = new TextInputDialog();
                daysDialog.setHeaderText("Введите количество дней:");
                daysDialog.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        daysDialog.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });
                daysDialog.showAndWait().ifPresent(days -> {
                    book.addReaderRecord(readerName, Integer.parseInt(days));
                    ObservableList<ReaderRecord> r = FXCollections.observableArrayList();
                    for (ReaderRecord record : book.getReaderRecords()) {
                        r.add(record);
                    }
                    readers.setAll(r);
                });
            });
        });
        //кнока удаления читателя с диалоговым окном ввода имени читателя
//        Button removeReaderButton = new Button("Удалить читателя");
//        removeReaderButton.setOnAction(e -> {
//            TextInputDialog removeReaderDialog = new TextInputDialog();
//            removeReaderDialog.setHeaderText("Введите имя читателя для удаления:");
//            removeReaderDialog.showAndWait().ifPresent(readerName -> {
//                book.removeReaderRecord(readerName);
//                ObservableList<ReaderRecord> r = FXCollections.observableArrayList();
//                for (ReaderRecord record : book.getReaderRecords()) {
//                    if(record.getReaderName().equals(readerName)) continue;
//                    r.add(record);
//                }
//                readers.setAll(r);
//            });
//        });

        Button showTotalUsage = new Button("Время пользования");
        //Логика отображения суммарного времени пользования книгой
        showTotalUsage.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Время использования книги");
            alert.setHeaderText(null);
            alert.setContentText("Общее количество дней использования книги: " + book.getTotalUsageDays());
            alert.showAndWait();
        });

        buttonBox.getChildren().addAll(addReaderButton, showTotalUsage);

        VBox vbox = new VBox(10, readerTable, buttonBox);
        Scene detailScene = new Scene(vbox, 400, 300);
        detailStage.setScene(detailScene);
        detailStage.show();
    }
}
