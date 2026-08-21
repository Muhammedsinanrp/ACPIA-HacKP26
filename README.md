# ACPIA — Kerala Cyber Police Investigation Portal

![ACPIA Banner](https://img.shields.io/badge/Platform-Android%20%7C%20Jetpack%20Compose-blue?style=for-the-badge)
![Security](https://img.shields.io/badge/Security-SHA--256%20Chain--of--Custody-green?style=for-the-badge)
![Legal Standard](https://img.shields.io/badge/Compliance-Sec%2065B%20Evidence%20Act-orange?style=for-the-badge)

## 📥 Download APK

[![Download ACPIA](https://img.shields.io/badge/Download-Latest%20APK-1F8ED6?style=for-the-bold&logo=android&logoColor=white)](https://github.com/Muhammedsinanrp/ACPIA-HacKP26/releases/latest)

---

A modern, high-security law-enforcement application built for the **Kerala Police Cyber Operations Wing (Cyberdome)** to streamline digital evidence management, suspect cross-correlation, and court-ready forensic audit logging.

---

## 🛡️ Core Features

- **Investigation Command Center**: Real-time KPI metrics, active case tracking, and incident escalation timeline.
- **FIR & Case Management**: 5-stage case lifecycle management (`Intake` → `Forensic Analysis` → `Suspect Mapping` → `Charge Sheet` → `Trial Court Ready`).
- **Cryptographic Evidence Vault**: Real-time **SHA-256** checksum calculation and verifiable chain-of-custody transfer logging.
- **Entity Correlation Graph**: Interactive node-link canvas mapping relationships between suspects, mobile devices (IMEI), bank/UPI accounts, and C2 servers.
- **Cyber Intelligence AI Co-Pilot**: Automated threat indicator matching, malware signature intelligence, and legal draft assistance.
- **Section 65B(4) Certificate Generator**: Generates court-admissible electronic evidence certificates compliant with the Indian Evidence Act.
- **Cyber Law & Forensics SOPs**: Quick reference guide for the Information Technology Act (Sections 66C, 66D, 66E, 67, 43) and forensic acquisition standards.

---

## 🏗️ Architecture & Tech Stack

- **UI Layer**: Kotlin + Jetpack Compose + Material 3
- **State Management**: MVVM with unidirectional data flow and Kotlin `StateFlow`
- **Security & Cryptography**: Native JVM `MessageDigest` (SHA-256), tamper-proof digital seals, and chained audit logs
- **Target Android SDK**: API 34 (Android 14)
- **Minimum Android SDK**: API 24 (Android 7.0)

---

## 🚀 Getting Started

### Building & Running
1. Clone this repository:
   ```bash
   git clone https://github.com/Muhammedsinanrp/ACPIA-HacKP26.git
   ```
2. Open the project directory in **Android Studio**.
3. Allow Gradle to sync dependencies.
4. Select an emulator or connected device and click **Run** (`Shift + F10`).
