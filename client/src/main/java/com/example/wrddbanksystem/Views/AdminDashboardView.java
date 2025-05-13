package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Controllers.Admin.DashboardController;
import com.example.wrddbanksystem.Core.Model;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.chart.PieChart;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

//----- Class To Create A Simplified Admin Dashboard View Showing Key Bank Metrics -----
public class AdminDashboardView {

    //----- Keep References To Updatable Elements -----
    private Text userCountText;
    private Text moneyInflowText;
    private PieChart accountDistributionChart;

    //----- Method To Create The View -----
    public AnchorPane createView() {
        try {
            //----- Create Main Container With Styling From CSS -----
            AnchorPane adminDashboardView = new AnchorPane();
            adminDashboardView.setPrefHeight(750);
            adminDashboardView.setPrefWidth(850);
            adminDashboardView.getStyleClass().add("dashboard_container");

            //----- Create Controller -----
            DashboardController dashboardController = new DashboardController();

            //----- Create Scroll Pane -----
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.getStyleClass().add("dashboard_scroll_pane");
            scrollPane.setPannable(true);

            AnchorPane.setTopAnchor(scrollPane, 0.0);
            AnchorPane.setBottomAnchor(scrollPane, 0.0);
            AnchorPane.setLeftAnchor(scrollPane, 0.0);
            AnchorPane.setRightAnchor(scrollPane, 0.0);

            //----- Create Content Container -----
            VBox contentContainer = new VBox();
            contentContainer.getStyleClass().add("dashboard_content");

            //----- Create Header Section -----
            HBox headerBox = createHeaderSection();

            //----- Create Key Metrics Section -----
            HBox metricsSection = createKeyMetricsSection(dashboardController);

            //----- Create Chart Section -----
            VBox chartSection = createChartSection();

            //----- Add All Sections To The Container -----
            contentContainer.getChildren().addAll(
                    headerBox,
                    metricsSection,
                    chartSection
            );

            scrollPane.setContent(contentContainer);
            adminDashboardView.getChildren().add(scrollPane);

            //----- Initialize The Controller And Load Data -----
            try {
                dashboardController.initialize(null, null);

                //----- Load Data Immediately -----
                Map<String, Object> stats = getSystemStatistics();
                updateDashboardWithData(stats, dashboardController);

                //----- Schedule Periodic Updates Every 30 Seconds -----
                javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                        new javafx.animation.KeyFrame(
                                javafx.util.Duration.seconds(30),
                                event -> {
                                    Map<String, Object> updatedStats = getSystemStatistics();
                                    updateDashboardWithData(updatedStats, dashboardController);
                                }
                        )
                );
                timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
                timeline.play();
            } catch (Exception e) {
                System.err.println("Error initializing dashboard controller: " + e.getMessage());
                e.printStackTrace();
            }

            return adminDashboardView;

        } catch (Exception e) {
            e.printStackTrace();
            //----- Fallback To Error View If Something Goes Wrong -----
            AnchorPane errorView = new AnchorPane();
            Label errorLabel = new Label("Error loading dashboard. Please try again later.");
            errorView.getChildren().add(errorLabel);
            AnchorPane.setTopAnchor(errorLabel, 10.0);
            AnchorPane.setLeftAnchor(errorLabel, 10.0);
            return errorView;
        }
    }

    //----- Method To Create The Dashboard Header Section -----
    private HBox createHeaderSection() {
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setSpacing(20);
        headerBox.setPadding(new Insets(0, 0, 15, 0));

        //----- Create Dashboard Title -----
        Text titleText = new Text("BANK METRICS DASHBOARD");
        titleText.getStyleClass().add("title");

        //----- Add Current Date -----
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
        Label dateLabel = new Label(today);
        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        VBox titleBox = new VBox(5);
        titleBox.getChildren().addAll(titleText, dateLabel);

        //----- Create Refresh Button With Icon -----
        Button refreshButton = new Button();
        refreshButton.getStyleClass().add("refresh_btn");

        FontAwesomeIconView refreshIcon = new FontAwesomeIconView(FontAwesomeIcon.REFRESH);
        refreshButton.setGraphic(refreshIcon);

        //----- Add Tooltip To Refresh Button -----
        Tooltip refreshTooltip = new Tooltip("Refresh dashboard data");
        refreshButton.setTooltip(refreshTooltip);

        refreshButton.setOnAction(e -> {
            //----- Show Spinning Animation -----
            refreshIcon.setRotate(0);
            javafx.animation.RotateTransition rotateTransition =
                    new javafx.animation.RotateTransition(Duration.millis(750), refreshIcon);
            rotateTransition.setByAngle(360);
            rotateTransition.setCycleCount(2);
            rotateTransition.play();

            refreshButton.setDisable(true);

            //----- Fetch Fresh Data -----
            javafx.application.Platform.runLater(() -> {
                try {
                    Map<String, Object> stats = getSystemStatistics();
                    DashboardController controller = new DashboardController();
                    updateDashboardWithData(stats, controller);

                    //----- Show A Success Indicator -----
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(1500), refreshButton);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.5);
                    fadeOut.setAutoReverse(true);
                    fadeOut.setCycleCount(2);
                    fadeOut.play();

                    refreshButton.setDisable(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    refreshButton.setDisable(false);
                }
            });
        });

        //----- Add Growing Region To Push Refresh Button To The Right -----
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox.getChildren().addAll(titleBox, spacer, refreshButton);
        return headerBox;
    }

    //----- Method To Create The Key Metrics Section -----
    private HBox createKeyMetricsSection(DashboardController controller) {
        //----- Create Container For Stats Cards -----
        HBox statsContainer = new HBox();
        statsContainer.getStyleClass().add("stats_container");

        //----- Create Total Users Card -----
        VBox userCountCard = createMetricCard("Total Bank Users", "0", "Total number of registered clients", FontAwesomeIcon.USERS);

        //----- Create Total Money Inflow Card -----
        VBox moneyInflowCard = createMetricCard("Total Money Inflow", "$0.00", "Sum of all account balances", FontAwesomeIcon.MONEY);

        //----- Connect To Controller And Save References For Updating -----
        userCountText = (Text) ((VBox) userCountCard.getChildren().get(1)).getChildren().get(0);
        controller.checking_count = userCountText;

        moneyInflowText = (Text) ((VBox) moneyInflowCard.getChildren().get(1)).getChildren().get(0);
        controller.savings_count = moneyInflowText;

        //----- Add Cards To Container -----
        statsContainer.getChildren().addAll(userCountCard, moneyInflowCard);
        HBox.setHgrow(userCountCard, Priority.ALWAYS);
        HBox.setHgrow(moneyInflowCard, Priority.ALWAYS);

        return statsContainer;
    }

    //----- Method To Create The Chart Section -----
    private VBox createChartSection() {
        VBox chartSection = new VBox();
        chartSection.getStyleClass().add("chart_section");
        chartSection.setPadding(new Insets(15));
        chartSection.setSpacing(15);

        //----- Create Section Header -----
        HBox sectionHeader = new HBox();
        sectionHeader.setAlignment(Pos.CENTER_LEFT);
        sectionHeader.setSpacing(10);

        FontAwesomeIconView chartIcon = new FontAwesomeIconView(FontAwesomeIcon.PIE_CHART);
        chartIcon.getStyleClass().add("section_icon");
        chartIcon.setSize("20");

        Text sectionTitle = new Text("Account Distribution");
        sectionTitle.getStyleClass().add("section_title");

        sectionHeader.getChildren().addAll(chartIcon, sectionTitle);

        //----- Create Pie Chart -----
        accountDistributionChart = new PieChart();
        accountDistributionChart.setTitle("");
        accountDistributionChart.setLabelsVisible(true);
        accountDistributionChart.setLegendVisible(true);
        accountDistributionChart.setPrefHeight(250);

        //----- Initial Empty Data -----
        accountDistributionChart.getData().add(new PieChart.Data("Checking", 1));
        accountDistributionChart.getData().add(new PieChart.Data("Savings", 1));

        //----- Style The Chart Data -----
        String[] colors = {"#003333", "#00695c"};
        int colorIndex = 0;
        for (PieChart.Data data : accountDistributionChart.getData()) {
            data.getNode().setStyle("-fx-pie-color: " + colors[colorIndex % colors.length] + ";");
            colorIndex++;
        }

        chartSection.getChildren().addAll(sectionHeader, accountDistributionChart);

        return chartSection;
    }

    //----- Method To Create A Metric Card With Styling From CSS -----
    private VBox createMetricCard(String title, String value, String description, FontAwesomeIcon iconType) {
        VBox card = new VBox();
        card.getStyleClass().add("stats_card");

        //----- Card Header With Title And Icon -----
        HBox cardHeader = new HBox();
        cardHeader.setAlignment(Pos.CENTER_LEFT);
        cardHeader.setSpacing(10);

        FontAwesomeIconView cardIcon = new FontAwesomeIconView(iconType);
        cardIcon.getStyleClass().add("card_icon");
        cardIcon.setSize("20");

        Text cardTitle = new Text(title);
        cardTitle.getStyleClass().add("card_title");

        cardHeader.getChildren().addAll(cardIcon, cardTitle);

        //----- Stat Value Box -----
        VBox statBox = new VBox();
        statBox.getStyleClass().add("stat_box");
        statBox.setAlignment(Pos.CENTER);

        Text statValue = new Text(value);
        statValue.getStyleClass().add("stat_value");

        Text statLabel = new Text(description);
        statLabel.getStyleClass().add("stat_label");

        statBox.getChildren().addAll(statValue, statLabel);

        //----- Add All To Card -----
        card.getChildren().addAll(cardHeader, statBox);

        //----- Add Hover Effect -----
        card.setOnMouseEntered(e -> card.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 5, 0, 0, 2);"));

        return card;
    }

    //----- Method To Get System Statistics From The Backend API -----
    private Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();

        try {
            //----- Get Real Data Using The Admin Service In Model -----
            Object adminStats = Model.getInstance().getAdminStatistics();

            if (adminStats != null && adminStats instanceof Map) {
                stats = (Map<String, Object>) adminStats;
                System.out.println("Retrieved stats: " + stats);
            } else {
                //----- Fallback To Mock Data For Testing -----
                stats.put("clientCount", 128);
                stats.put("checkingAccountCount", 95);
                stats.put("savingsAccountCount", 76);
                stats.put("totalInflow", 1254360.75);
                System.out.println("Using mock data since actual data is null or invalid");
            }
        } catch (Exception e) {
            System.err.println("Error getting system statistics: " + e.getMessage());
            e.printStackTrace();
            // Fallback to mock data for testing
            stats.put("clientCount", 128);
            stats.put("checkingAccountCount", 95);
            stats.put("savingsAccountCount", 76);
            stats.put("totalInflow", 1254360.75);
        }

        return stats;
    }

    //----- Method To Update The Dashboard With Fetched Data -----
    private void updateDashboardWithData(Map<String, Object> stats, DashboardController controller) {
        System.out.println("Updating dashboard with data: " + stats);

        //----- Update User Count -----
        if (stats.containsKey("clientCount")) {
            int clientCount = ((Number) stats.get("clientCount")).intValue();
            userCountText.setText(String.valueOf(clientCount));
            if (controller.checking_count != null) {
                controller.checking_count.setText(String.valueOf(clientCount));
            }
        }

        //----- Update Total Money Inflow -----
        if (stats.containsKey("totalInflow")) {
            double totalInflow = ((Number) stats.get("totalInflow")).doubleValue();
            String formattedInflow = String.format("$%,.2f", totalInflow);
            moneyInflowText.setText(formattedInflow);

            if (controller.savings_count != null) {
                controller.savings_count.setText(formattedInflow);
            }
        }

        //----- Update Account Distribution Chart -----
        if (stats.containsKey("checkingAccountCount") && stats.containsKey("savingsAccountCount")) {
            int checkingCount = ((Number) stats.get("checkingAccountCount")).intValue();
            int savingsCount = ((Number) stats.get("savingsAccountCount")).intValue();

            accountDistributionChart.getData().clear();
            accountDistributionChart.getData().add(new PieChart.Data("Checking (" + checkingCount + ")", checkingCount));
            accountDistributionChart.getData().add(new PieChart.Data("Savings (" + savingsCount + ")", savingsCount));

            //----- Re-Apply Colors -----
            String[] colors = {"#003333", "#00695c"};
            int colorIndex = 0;
            for (PieChart.Data data : accountDistributionChart.getData()) {
                data.getNode().setStyle("-fx-pie-color: " + colors[colorIndex % colors.length] + ";");
                colorIndex++;
            }
        }
    }
}