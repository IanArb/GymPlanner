//
//  ProfileView.swift
//  iosApp
//
//  Created by Ian Arbuckle on 10/06/2026.
//  Copyright © 2026 orgName. All rights reserved.
//

import SwiftUI

struct ProfileView: View {
    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Image(systemName: "person.circle.fill")
                    .font(.system(size: 80))
                    .foregroundColor(.blue)
                
                Text("Profile")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                
                Text("Your profile information will appear here")
                    .font(.body)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
            }
            .navigationTitle("Profile")
        }
    }
}

#Preview {
    ProfileView()
}
