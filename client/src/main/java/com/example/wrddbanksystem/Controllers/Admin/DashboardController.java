package com.example.wrddbanksystem.Controllers.Admin;

import com.example.wrddbanksystem.Models.ApiClient;
import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardController {
    //----- Account Statistics -----
    public Text checking_count;
    public Text savings_count;
    public Button refresh_accounts_btn;

    //----- System Health -----
    public Text system_status;
    public Text db_status;
    public Text last_backup;

    //----- Transaction Volume Chart -----
    public ComboBox<String> transaction_period_selector;
    public LineChart<String, Number> transaction_chart;
    public CategoryAxis x_axis;
    public NumberAxis y_axis;

    //----- Account Growth Chart -----
    public StackedBarChart<String, Number> account_growth_chart;

    //----- Initialize The Dashboard Controller -----
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupUI();
        loadData();
        setupListeners();
    }

    //----- Method To Setup The UI -----
    private void setupUI() {
        transaction_period_selector.setItems(FXCollections.observableArrayList(
                "Daily", "Weekly", "Monthly", "Yearly"
        ));
        transaction_period_selector.setValue("Monthly");

        LocalDate today = LocalDate.now();
        last_backup.setText(today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 08:00");

        transaction_chart.setTitle("Transaction Volume");
        account_growth_chart.setTitle("Account Growth Over Time");
    }

    //----- Method To Load The Data -----
    private void loadData() {
        loadAccountStatistics();
        loadTransactionData();
        loadAccountGrowthData();
        checkSystemHealth();
    }

    //----- Method To Setup The Listeners -----
    private void setupListeners() {
        refresh_accounts_btn.setOnAction(e -> {
            loadAccountStatistics();
        });

        transaction_period_selector.setOnAction(e -> {
            loadTransactionData();
        });
    }

    //----- Method To Load The Account Statistics -----
    private void loadAccountStatistics() {
        try {
            //----- Get Statistics From API -----
            Map<String, Object> statistics = ApiClient.get("/admin/statistics", Map.class);

            if (statistics != null) {
                int clientCount = ((Number) statistics.get("clientCount")).intValue();
                int checkingCount = ((Number) statistics.get("checkingAccountCount")).intValue();
                int savingsCount = ((Number) statistics.get("savingsAccountCount")).intValue();

                checking_count.setText(String.valueOf(clientCount));
                savings_count.setText(String.valueOf(checkingCount + savingsCount));
            } else {
                checking_count.setText("N/A");
                savings_count.setText("N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
            checking_count.setText("Error");
            savings_count.setText("Error");
        }
    }

    //----- Method To Load The Transaction Data -----
    private void loadTransactionData() {
        transaction_chart.getData().clear();

        XYChart.Series<String, Number> depositSeries = new XYChart.Series<>();
        depositSeries.setName("Deposits");

        XYChart.Series<String, Number> withdrawalSeries = new XYChart.Series<>();
        withdrawalSeries.setName("Withdrawals");

        String period = transaction_period_selector.getValue();

        try {
            //----- Get Transaction Data From API -----
            Map<String, Object> transactionData = ApiClient.get("/admin/transactions/summary?period=" + period.toLowerCase(), Map.class);

            if (transactionData != null && transactionData.containsKey("periodData")) {
                Map<String, Map<String, Number>> periodData = (Map<String, Map<String, Number>>) transactionData.get("periodData");

                for (Map.Entry<String, Map<String, Number>> entry : periodData.entrySet()) {
                    String periodLabel = entry.getKey();
                    Number deposits = entry.getValue().get("deposits");
                    Number withdrawals = entry.getValue().get("withdrawals");

                    depositSeries.getData().add(new XYChart.Data<>(periodLabel, deposits));
                    withdrawalSeries.getData().add(new XYChart.Data<>(periodLabel, withdrawals));
                }
            } else {
                //----- Fallback To Mock Data If API Doesn't Return Expected Format -----
                provideMockTransactionData(period, depositSeries, withdrawalSeries);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            provideMockTransactionData(period, depositSeries, withdrawalSeries);
        }

        transaction_chart.getData().addAll(depositSeries, withdrawalSeries);
    }

    //----- Method To Provide Mock Transaction Data -----
    private void provideMockTransactionData(String period, XYChart.Series<String, Number> depositSeries, XYChart.Series<String, Number> withdrawalSeries) {
        if ("Monthly".equals(period)) {
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
            double[] deposits = {4500, 5200, 4800, 6700, 5900, 7000};
            double[] withdrawals = {3200, 3800, 3600, 4100, 3900, 4500};

            for (int i = 0; i < months.length; i++) {
                depositSeries.getData().add(new XYChart.Data<>(months[i], deposits[i]));
                withdrawalSeries.getData().add(new XYChart.Data<>(months[i], withdrawals[i]));
            }
        } else if ("Weekly".equals(period)) {
            for (int i = 1; i <= 6; i++) {
                depositSeries.getData().add(new XYChart.Data<>("Week " + i, 1000 + Math.random() * 1000));
                withdrawalSeries.getData().add(new XYChart.Data<>("Week " + i, 800 + Math.random() * 800));
            }
        } else if ("Daily".equals(period)) {
            for (int i = 1; i <= 7; i++) {
                depositSeries.getData().add(new XYChart.Data<>("Day " + i, 200 + Math.random() * 300));
                withdrawalSeries.getData().add(new XYChart.Data<>("Day " + i, 150 + Math.random() * 250));
            }
        } else {
            for (int i = 2021; i <= 2023; i++) {
                depositSeries.getData().add(new XYChart.Data<>(String.valueOf(i), 50000 + Math.random() * 20000));
                withdrawalSeries.getData().add(new XYChart.Data<>(String.valueOf(i), 40000 + Math.random() * 15000));
            }
        }
    }

    //----- Method To Load The Account Growth Data -----
    private void loadAccountGrowthData() {
        account_growth_chart.getData().clear();

        XYChart.Series<String, Number> checkingSeries = new XYChart.Series<>();
        checkingSeries.setName("Checking Accounts");

        XYChart.Series<String, Number> savingsSeries = new XYChart.Series<>();
        savingsSeries.setName("Savings Accounts");

        try {
            //----- Get Account Growth Data From API -----
            Map<String, Object> growthData = ApiClient.get("/admin/accounts/growth", Map.class);

            if (growthData != null && growthData.containsKey("monthlyGrowth")) {
                Map<String, Map<String, Number>> monthlyGrowth = (Map<String, Map<String, Number>>) growthData.get("monthlyGrowth");

                for (Map.Entry<String, Map<String, Number>> entry : monthlyGrowth.entrySet()) {
                    String month = entry.getKey();
                    Number checking = entry.getValue().get("checking");
                    Number savings = entry.getValue().get("savings");

                    checkingSeries.getData().add(new XYChart.Data<>(month, checking));
                    savingsSeries.getData().add(new XYChart.Data<>(month, savings));
                }
            } else {
                //----- Fallback To Mock Data If API Doesn't Return Expected Format -----
                provideMockAccountGrowthData(checkingSeries, savingsSeries);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            provideMockAccountGrowthData(checkingSeries, savingsSeries);
        }

        account_growth_chart.getData().addAll(checkingSeries, savingsSeries);
    }

    //----- Method To Provide Mock Account Growth Data -----
    private void provideMockAccountGrowthData(XYChart.Series<String, Number> checkingSeries, XYChart.Series<String, Number> savingsSeries) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
        int[] checkingData = {45, 52, 59, 63, 71, 77};
        int[] savingsData = {30, 36, 42, 50, 59, 63};

        for (int i = 0; i < months.length; i++) {
            checkingSeries.getData().add(new XYChart.Data<>(months[i], checkingData[i]));
            savingsSeries.getData().add(new XYChart.Data<>(months[i], savingsData[i]));
        }
    }

    //----- Method To Check System Health -----
    private void checkSystemHealth() {
        try {
            //----- Get System Health From API -----
            Map<String, Object> healthData = ApiClient.get("/admin/system/health", Map.class);

            if (healthData != null) {
                boolean systemStatus = (Boolean) healthData.get("systemStatus");
                boolean dbStatus = (Boolean) healthData.get("dbStatus");
                String lastBackup = (String) healthData.get("lastBackup");

                this.system_status.setText(systemStatus ? "Online" : "Offline");
                this.system_status.setStyle(systemStatus ? "-fx-fill: green;" : "-fx-fill: red;");

                this.db_status.setText(dbStatus ? "Connected" : "Disconnected");
                this.db_status.setStyle(dbStatus ? "-fx-fill: green;" : "-fx-fill: red;");

                if (lastBackup != null && !lastBackup.isEmpty()) {
                    this.last_backup.setText(lastBackup);
                }
            } else {
                //----- Set Default Values If API Call Fails -----
                system_status.setText("Online");
                system_status.setStyle("-fx-fill: green;");
                db_status.setText("Connected");
                db_status.setStyle("-fx-fill: green;");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            system_status.setText("Unknown");
            system_status.setStyle("-fx-fill: orange;");
            db_status.setText("Unknown");
            db_status.setStyle("-fx-fill: orange;");
        }
    }

    //----- Method To Get Client Count -----
    private int getClientCount() throws IOException, InterruptedException {
        Map<String, Object> statistics = ApiClient.get("/admin/statistics", Map.class);
        if (statistics != null && statistics.containsKey("clientCount")) {
            return ((Number) statistics.get("clientCount")).intValue();
        }
        return 0;
    }

    //----- Method To Get Checking Account Count -----
    private int getCheckingAccountCount() throws IOException, InterruptedException {
        Map<String, Object> statistics = ApiClient.get("/admin/statistics", Map.class);
        if (statistics != null && statistics.containsKey("checkingAccountCount")) {
            return ((Number) statistics.get("checkingAccountCount")).intValue();
        }
        return 0;
    }

    //----- Method To Get Savings Account Count -----
    private int getSavingsAccountCount() throws IOException, InterruptedException {
        Map<String, Object> statistics = ApiClient.get("/admin/statistics", Map.class);
        if (statistics != null && statistics.containsKey("savingsAccountCount")) {
            return ((Number) statistics.get("savingsAccountCount")).intValue();
        }
        return 0;
    }
} 