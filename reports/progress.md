# CWE-798 Vulnerability Remediation Progress

**Project**: Vitrail Clinic - Game Backend (`d:\HUH\game`)
**Language**: Java 17 (Spring Boot 3.1.4)
**Goal**: Scan and resolve CWE-798 (Use of Hard-coded Credentials) vulnerabilities
**Date**: 2026-03-19

## General

| Item | Value |
|------|-------|
| Previous Branch | N/A (no initial commit) |
| New Branch | `fix/cwe-798-hardcoded-credentials` |
| Build Tool | Maven (`C:\Program Files\Apache\maven\bin`) |
| JDK | Java 17 (`C:\Program Files\Java\jdk-17\bin`) |

## Findings

### CWE-798 Vulnerabilities Identified

| # | File | Location | Type |
|---|------|----------|------|
| 1 | `src/main/resources/application.yml` | Lines 7–8 | Hardcoded DB `username` and `password` |

### Java Source Files Scanned — No Hardcoded Credentials Found

- `GameBackendApplication.java` — clean
- `controller/CaseController.java` — clean
- `service/CaseService.java` — clean
- `repository/CaseRepository.java` — clean
- `model/CaseEntity.java` — clean

## Progress

- [✅] Migration Plan Generated ([plan.md](./plan.md))
- [⌛️] Version Control Setup (creating branch `fix/cwe-798-hardcoded-credentials`)
- Code Migration
    - [⌛️] `src/main/resources/application.yml` — replace hardcoded credentials with env vars
- Validation & Fixing
    - Build Environment
        - [✅] JAVA_HOME set to `C:\Program Files\Java\jdk-17`
        - [✅] MAVEN_HOME set to `C:\Program Files\Apache\maven`
    - [ ] Build and Fix
    - [ ] CVE Check
    - [ ] Completeness Check
    - [ ] Build Validation
- [ ] Final Summary
    - [ ] Final Code Commit
    - [ ] Migration Summary Generation
