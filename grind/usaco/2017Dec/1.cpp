#include <bits/stdc++.h>
#define int long long
#define double long double
#define V vector
#define NL "\n"
using namespace std;
signed main() {
    cin.tie(0)->sync_with_stdio(0);
    freopen("piepie.in", "r", stdin);
    freopen("piepie.out", "w", stdout);
    int n, d;
    cin >> n >> d;
    V<int> bessie_list(2 * n), elsie_list(2 * n);
    for (int i = 0; i < 2 * n; i++) {
        int x, y;
        cin >> x >> y;
        bessie_list[i] = x;
        elsie_list[i] = y;
    }
    queue<array<int, 2>> bfs;
    V<int> dp(2 * n, -1);
    auto comp = [](const pair<int, int> &a, const pair<int, int> &b) {
        if (a.first != b.first) {
            return a.first < b.first;
        }
        return a.second < b.second;
    };
    set<pair<int, int>, decltype(comp)> bessie_set(comp), elsie_set(comp);
    for (int i = 0; i < n; i++) {
        elsie_set.insert({elsie_list[i], i});
    }
    for (int i = n; i < 2 * n; i++) {
        bessie_set.insert({bessie_list[i], i});
    }
    for (int i = 0; i < n; i++) {
        if (elsie_list[i] == 0) {
            elsie_set.erase({elsie_list[i], i});
            dp[i] = 1;
            bfs.push({i, 1});
        }
    }
    for (int i = n; i < 2 * n; i++) {
        if (bessie_list[i] == 0) {
            bessie_set.erase({bessie_list[i], i});
            dp[i] = 1;
            bfs.push({i, 1});
        }
    }
    while (!bfs.empty()) {
        int index = bfs.front()[0], distance = bfs.front()[1];
        bfs.pop();
        if (index < n) {
            auto lower = bessie_set.lower_bound({bessie_list[index] - d, 0});
            auto upper = bessie_set.upper_bound({bessie_list[index], 2 * n});
            for (auto p = lower; p != upper; p++) {
                dp[p->second] = distance + 1;
                bfs.push({p->second, distance + 1});
            }
            bessie_set.erase(lower, upper);
        } else {
            auto lower = elsie_set.lower_bound({elsie_list[index] - d, 0});
            auto upper = elsie_set.upper_bound({elsie_list[index], 2 * n});
            for (auto p = lower; p != upper; p++) {
                dp[p->second] = distance + 1;
                bfs.push({p->second, distance + 1});
            }
            elsie_set.erase(lower, upper);
        }
    }
    for (int i = 0; i < n; i++) {
        cout << dp[i] << NL;
    }
}
