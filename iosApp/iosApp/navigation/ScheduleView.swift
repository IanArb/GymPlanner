//
//  ScheduleView.swift
//  iosApp
//
//  Created by Ian Arbuckle on 10/06/2026.
//  Copyright © 2026 orgName. All rights reserved.
//

import SwiftUI

struct ScheduleView: View {
    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Image(systemName: "calendar")
                    .font(.system(size: 80))
                    .foregroundColor(.blue)

                Text("Schedule")
                    .font(.largeTitle)
                    .fontWeight(.bold)

                Text("Your scheduled classes will appear here")
                    .font(.body)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
            }
            .navigationTitle("Schedule")
        }
    }
}

#Preview {
    ScheduleView()
}
