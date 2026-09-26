//
//  DashboardView.swift
//  iosApp
//
//  Created by Ian Arbuckle on 10/06/2026.
//  Copyright © 2026 orgName. All rights reserved.
//

import Foundation
import SharedGymPlanner
import SwiftUI

struct DashboardView: View {
    @StateObject private var viewModel: DashboardViewModel

    init(
        viewModel: DashboardViewModel = DashboardViewModel(
            repository: DefaultFitnessClassRepository()
        )
    ) {
        _viewModel = StateObject(wrappedValue: viewModel)
    }

    /// The current weekday as an uppercased English name (e.g. "MONDAY"),
    /// matching the format the API expects (see the Android app's `DayOfWeek.name`).
    private var currentDayOfWeek: String {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "EEEE"
        return formatter.string(from: Date()).uppercased()
    }

    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                contentView
            }
            .navigationTitle("Dashboard")
            .navigationBarTitleDisplayMode(.large)
        }
        .task {
            await viewModel.loadClasses(dayOfWeek: currentDayOfWeek)
        }
    }

    // MARK: - Content View

    @ViewBuilder
    private var contentView: some View {
        switch viewModel.uiState {
        case .idle, .loading:
            loadingView
        case let .success(classes):
            successView(classes: classes)
        case .error:
            errorView
        }
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
                Task { await viewModel.loadClasses(dayOfWeek: currentDayOfWeek) }
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
