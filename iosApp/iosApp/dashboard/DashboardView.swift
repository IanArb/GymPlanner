//
//  DashboardView.swift
//  iosApp
//
//  Created by Ian Arbuckle on 10/06/2026.
//  Copyright © 2026 orgName. All rights reserved.
//

import SwiftUI
import Foundation

struct DashboardView: View {
    @State private var selectedDay: String = "Monday"
    @State private var isLoading: Bool = false
    
    let daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
    
    // Sample data
    let sampleClasses: [FitnessClass] = [
        FitnessClass(
            id: "1",
            name: "Yoga Flow",
            description: "Gentle flow yoga class perfect for all levels. Focus on breath and movement.",
            imageUrl: "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b",
            startTime: Date().addingTimeInterval(3600),
            endTime: Date().addingTimeInterval(7200)
        ),
        FitnessClass(
            id: "2",
            name: "HIIT Training",
            description: "High-intensity interval training to boost your metabolism and build strength.",
            imageUrl: "https://images.unsplash.com/photo-1534258936925-c58bed479fcb",
            startTime: Date().addingTimeInterval(10800),
            endTime: Date().addingTimeInterval(14400)
        ),
        FitnessClass(
            id: "3",
            name: "Spin Class",
            description: "Indoor cycling class with energizing music and motivating instructors.",
            imageUrl: "https://images.unsplash.com/photo-1541534741688-6078c6bfb5c5",
            startTime: Date().addingTimeInterval(18000),
            endTime: Date().addingTimeInterval(21600)
        ),
        FitnessClass(
            id: "4",
            name: "Pilates Core",
            description: "Strengthen your core with focused Pilates exercises and techniques.",
            imageUrl: "https://images.unsplash.com/photo-1518611012118-696072aa579a",
            startTime: Date().addingTimeInterval(25200),
            endTime: Date().addingTimeInterval(28800)
        )
    ]
    
    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                // Content
                contentView
            }
            .navigationTitle("Dashboard")
            .navigationBarTitleDisplayMode(.large)
        }
    }

    
    // MARK: - Content View
    
    @ViewBuilder
    private var contentView: some View {
        if isLoading {
            loadingView
        } else {
            successView(classes: sampleClasses)
        }
    }
    
    private var idleView: some View {
        VStack(spacing: 16) {
            Image(systemName: "figure.strengthtraining.traditional")
                .font(.system(size: 60))
                .foregroundColor(.gray)
            
            Text("Select a day to view classes")
                .font(.headline)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
    
    private var loadingView: some View {
        VStack(spacing: 16) {
            ProgressView()
                .scaleEffect(1.5)
            
            Text("Loading classes...")
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
    
    private func successView(classes: [FitnessClass]) -> some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                // Today's Classes Header
                HStack {
                    Text("Today's Classes")
                        .font(.system(size: 16, weight: .bold))
                    
                    Spacer()
                    
                    Button(action: {
                        // TODO: Navigate to weekly schedule
                    }) {
                        Text("View Weekly Schedule")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.blue)
                    }
                }
                .padding(.horizontal)
                
                // Carousel
                if classes.isEmpty {
                    emptyStateView
                } else {
                    GymClassesCarousel(classes: classes)
                        .frame(height: 350)
                }
                
                Spacer()
                    .frame(height: 16)
            }
            .padding(.top, 16)
        }
    }
    
    private var emptyStateView: some View {
        VStack(spacing: 16) {
            Image(systemName: "calendar.badge.exclamationmark")
                .font(.system(size: 60))
                .foregroundColor(.gray)
            
            Text("No classes scheduled")
                .font(.headline)
                .foregroundColor(.secondary)
            
            Text("Check back later or try another day")
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
    }
    
    private var errorView: some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.triangle")
                .font(.system(size: 60))
                .foregroundColor(.red)
            
            Text("Oops! Something went wrong")
                .font(.headline)
                .foregroundColor(.primary)
            
            Text("Unable to load fitness classes")
                .font(.subheadline)
                .foregroundColor(.secondary)
            
            Button(action: {
                // Retry action - for now just toggle loading
                isLoading.toggle()
            }) {
                Text("Try Again")
                    .font(.headline)
                    .foregroundColor(.white)
                    .padding(.horizontal, 32)
                    .padding(.vertical, 12)
                    .background(Color.blue)
                    .cornerRadius(10)
            }
            .padding(.top, 8)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

// MARK: - Preview

#Preview {
    DashboardView()
}
