Crypto Trading Simulator (Java)

This is a console-based paper trading application written in Java. It allows users to create an account, manage a virtual portfolio, and simulate buying/selling cryptocurrency using real-time market data.

This project was originally developed as a final project for my Grade 11 Computer Science course.

Features:
*User Authentication: Secure sign-up and login system for multiple users.
*Portfolio Management: Users are allocated a starting CAD balance and can track their virtual portfolio.
*Real-Time Price Data: Integrates the CoinGecko API to fetch live prices for Bitcoin (BTC), Ethereum (ETH), and Solana (SOL).
*Buy/Sell Functionality: Users can simulate market orders, with their CAD and crypto balances updating accordingly.
*Data Persistence: All user accounts, passwords, and portfolio balances are saved to and loaded from CSV flat-files.

Technologies Used:
*Java
*CoinGecko API (for real-time price data)
*CSV (for data storage)

Future Improvements:
*SQL Database Migration: I am currently in the process of migrating the data persistence layer from CSV files to a SQL based database.
*Tracking profit and loss, possibly over certain periods of time.
*Web Interface: A potential future goal is to build a simple web-based UI for this application.