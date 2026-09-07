//
//  MainTabView.swift
//  iosApp
//
//  Created by Ian Arbuckle on 28/12/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct MainTabView: View {
    @State private var selectedTab = 0
    
    var body: some View {
        VStack(spacing: 0) {
            // Content
            Group {
                switch selectedTab {
                case 0:
                    DashboardView()
                case 1:
                    ScheduleView()
                case 2:
                    ProfileView()
                default:
                    DashboardView()
                }
            }
            
            // Custom Tab Bar
            CustomTabBar(selectedTab: $selectedTab)
        }
        .ignoresSafeArea(.keyboard)
    }
}

// MARK: - Custom Tab Bar

struct CustomTabBar: View {
    @Binding var selectedTab: Int
    
    var body: some View {
        HStack(spacing: 0) {
            // Home Tab
            TabBarItem(
                icon: "house.fill",
                title: "Dashboard",
                isSelected: selectedTab == 0
            ) {
                selectedTab = 0
            }
            
            // Schedule Tab
            TabBarItem(
                icon: "calendar",
                title: "Schedule",
                isSelected: selectedTab == 1
            ) {
                selectedTab = 1
            }
            
            // Profile Tab
            TabBarItem(
                icon: "person.fill",
                title: "Profile",
                isSelected: selectedTab == 2
            ) {
                selectedTab = 2
            }
        }
        .frame(height: 60)
        .background(Color(.systemBackground))
        .overlay(
            Rectangle()
                .frame(height: 0.5)
                .foregroundColor(Color.gray.opacity(0.3)),
            alignment: .top
        )
    }
}

// MARK: - Tab Bar Item

struct TabBarItem: View {
    let icon: String
    let title: String
    let isSelected: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            VStack(spacing: 4) {
                Image(systemName: icon)
                    .font(.system(size: 20))
                
                Text(title)
                    .font(.system(size: 10))
            }
            .foregroundColor(isSelected ? .blue : .gray)
            .frame(maxWidth: .infinity)
            .contentShape(Rectangle())
        }
    }
}

// MARK: - Preview

#Preview {
    MainTabView()
}
