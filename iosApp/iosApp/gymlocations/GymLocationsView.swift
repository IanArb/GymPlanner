//
//  GymLocationsView.swift
//  iosApp
//
//  Created by Ian Arbuckle on 24/09/2026.
//  Copyright © 2026 orgName. All rights reserved.
//

import SharedGymPlanner
import SwiftUI

struct GymLocationsView: View {
    @StateObject private var viewModel: GymLocationsViewModel

    init(
        viewModel: GymLocationsViewModel = GymLocationsViewModel(
            repository: DefaultGymLocationsRepository()
        )
    ) {
        _viewModel = StateObject(wrappedValue: viewModel)
    }

    var body: some View {
        NavigationView {
            content
                .navigationTitle("Gym Locations")
                .navigationBarTitleDisplayMode(.large)
        }
        .task {
            await viewModel.loadGymLocations()
        }
    }

    // MARK: - Content

    @ViewBuilder
    private var content: some View {
        switch viewModel.uiState {
        case .idle, .loading:
            loadingView
        case let .success(locations):
            successView(locations: locations)
        case .error:
            errorView
        }
    }

    private var loadingView: some View {
        VStack(spacing: 16) {
            ProgressView()
                .scaleEffect(1.5)

            Text("Loading gym locations...")
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    private let columns = [
        GridItem(.flexible(), spacing: 16),
        GridItem(.flexible(), spacing: 16),
    ]

    @ViewBuilder
    private func successView(locations: [GymLocation]) -> some View {
        if locations.isEmpty {
            emptyStateView
        } else {
            ScrollView {
                LazyVGrid(columns: columns, spacing: 16) {
                    ForEach(locations) { location in
                        GymLocationCard(location: location)
                    }
                }
                .padding(16)
            }
        }
    }

    private var emptyStateView: some View {
        VStack(spacing: 16) {
            Image(systemName: "mappin.slash")
                .font(.system(size: 60))
                .foregroundColor(.gray)

            Text("No gym locations available")
                .font(.headline)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    private var errorView: some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.triangle")
                .font(.system(size: 60))
                .foregroundColor(.red)

            Text("Failed to retrieve gym locations.")
                .font(.headline)
                .foregroundColor(.primary)
                .multilineTextAlignment(.center)

            Button(action: {
                Task { await viewModel.loadGymLocations() }
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
        .padding()
    }
}

// MARK: - Gym Location Card

struct GymLocationCard: View {
    let location: GymLocation

    private let cardBackground = Color(.secondarySystemGroupedBackground)

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            AsyncImage(url: URL(string: location.imageUrl)) { phase in
                switch phase {
                case .empty:
                    placeholder { ProgressView() }
                case let .success(image):
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                case .failure:
                    // Centered icon over the card surface, matching the Android design.
                    placeholder {
                        Image(systemName: "photo")
                            .font(.system(size: 44))
                            .foregroundColor(.secondary)
                    }
                @unknown default:
                    placeholder { EmptyView() }
                }
            }
            .frame(maxWidth: .infinity)
            .frame(height: 120)
            .clipped()

            VStack(alignment: .leading, spacing: 4) {
                Text(location.title)
                    .font(.system(size: 16, weight: .bold))
                    .foregroundStyle(.primary)

                Text(location.subTitle)
                    .font(.system(size: 14))
                    .foregroundStyle(.secondary)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(16)
        }
        .background(cardBackground)
        .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
        .shadow(color: .black.opacity(0.1), radius: 8, y: 2)
    }

    /// A full-width placeholder that blends onto the card surface (as in the
    /// Android reference) with the supplied content centered.
    private func placeholder(@ViewBuilder content: () -> some View) -> some View {
        cardBackground.overlay(content())
    }
}

// MARK: - Preview

#Preview {
    GymLocationCard(
        location: GymLocation(
            id: "1",
            title: "GymPlanner City Centre",
            subTitle: "Dublin 2",
            description: "Our flagship gym in the heart of the city.",
            imageUrl: "https://images.unsplash.com/photo-1534438327276-14e5300c3a48"
        )
    )
}
