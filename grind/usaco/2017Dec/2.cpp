#include <bits/stdc++.h>
#define int long long
#define double long double
#define V vector
#define NL "\n"
using namespace std;
const int MOD = 1e9 + 7;
int rec(int i, int c, int p, V<V<int>> &graph, V<int> &colors, V<V<int>> &dp) {
    if (dp[i][c] != -1) {
        return dp[i][c];
    }
    if (colors[i] && colors[i] != c) {
        return 0;
    }
    int ans = 1;
    for (int j : graph[i]) {
        if (j == p) {
            continue;
        }
        int inner_ans = 0;
        for (int d = 1; d <= 3; d++) {
            if (c == d || (colors[j] && colors[j] != d)) {
                continue;
            }
            bool set = !colors[j];
            colors[j] = d;
            inner_ans = (inner_ans + rec(j, d, i, graph, colors, dp)) % MOD;
            if (set) {
                colors[j] = 0;
            }
        }
        ans = (ans * inner_ans) % MOD;
    }
    return dp[i][c] = ans;
}
signed main() {
    cin.tie(0)->sync_with_stdio(0);
    freopen("barnpainting.in", "r", stdin);
    freopen("barnpainting.out", "w", stdout);
    int n, k;
    cin >> n >> k;
    V<V<int>> graph(n);
    for (int i = 0; i < n - 1; i++) {
        int x, y;
        cin >> x >> y;
        graph[x - 1].push_back(y - 1);
        graph[y - 1].push_back(x - 1);
    }
    V<int> colors(n);
    for (int i = 0; i < k; i++) {
        int x, y;
        cin >> x >> y;
        colors[x - 1] = y;
    }
    V<V<int>> dp(n, V<int>(4, -1));
    int ans = 0;
    for (int c = 1; c <= 3; c++) {
        ans = (ans + rec(0, c, -1, graph, colors, dp)) % MOD;
    }
    cout << ans << NL;
}
