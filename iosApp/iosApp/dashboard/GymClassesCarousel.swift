//
//  GymClassesCarousel.swift
//  iosApp
//
//  Created by Ian Arbuckle on 14/06/2026.
//  Copyright © 2026 orgName. All rights reserved.
//

import SwiftUI

struct GymClassesCarousel: View {
    let classes: [FitnessClass]
    
    var body: some View {
        if classes.isEmpty {
            Text("No classes available at the moment.")
                .padding(16)
                .font(.body)
                .foregroundColor(.secondary)
        } else {
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 16) {
                    ForEach(classes) { fitnessClass in
                        GymClassCarouselCard(fitnessClass: fitnessClass)
                            .frame(width: 320)
                    }
                }
                .padding(.horizontal, 16)
            }
        }
    }
}

// MARK: - Gym Class Carousel Card

struct GymClassCarouselCard: View {
    let fitnessClass: FitnessClass
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            // Image
            AsyncImage(url: URL(string: fitnessClass.imageUrl)) { phase in
                switch phase {
                case .empty:
                    Rectangle()
                        .fill(Color(.systemGray6))
                        .overlay(
                            ProgressView()
                        )
                case .success(let image):
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                case .failure:
                    Rectangle()
                        .fill(Color(.systemGray6))
                        .overlay(
                            Image(systemName: "photo")
                                .font(.largeTitle)
                                .foregroundColor(.secondary)
                        )
                @unknown default:
                    EmptyView()
                }
            }
            .frame(height: 200)
            .clipped()
            
            // Content
            VStack(alignment: .leading, spacing: 8) {
                Text(fitnessClass.name)
                    .font(.headline)
                    .foregroundStyle(.primary)
                
                Text(fitnessClass.description)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                    .lineLimit(2)
                
                Spacer()
                    .frame(height: 10)
                
                Label(classTimeString, systemImage: "clock")
                    .font(.caption)
                    .foregroundStyle(.secondary)
                
                Spacer()
                    .frame(height: 4)
            }
            .padding(16)
        }
        .background(Color(.secondarySystemGroupedBackground))
        .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
    }
    
    private var classTimeString: String {
        let formatter = DateFormatter()
        formatter.timeStyle = .short
        let startTime = formatter.string(from: fitnessClass.startTime)
        let endTime = formatter.string(from: fitnessClass.endTime)
        return "\(startTime) - \(endTime)"
    }
}

// MARK: - Preview

#Preview {
    VStack {
        GymClassesCarousel(classes: [
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
        ])
        .frame(height: 350)
        
        Spacer()
    }
}
