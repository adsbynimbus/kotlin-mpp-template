import SwiftUI
import core

struct ContentView: View {
    var body: some View {
        Text(Platform().platform)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
