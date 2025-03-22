#include <bits/stdc++.h>
#define int long long
#define double long double
#define V vector
#define NL "\n"
using namespace std;
signed main() {
    cin.tie(0)->sync_with_stdio(0);
    freopen("hayfeast.in", "r", stdin);
    freopen("hayfeast.out", "w", stdout);
    int n, m;
    cin >> n >> m;
    V<int> f(n), s(n);
    for (int i = 0; i < n; i++) {
        cin >> f[i] >> s[i];
    }
    multiset<int> st;
    int j = 0, fsum = 0, mn = LLONG_MAX;
    for (int i = 0; i < n; i++) {
        while (j < n && fsum < m) {
            fsum += f[j];
            st.insert(s[j]);
            j++;
        }
        if (fsum >= m) {
            mn = min(mn, *st.rbegin());
        }
        st.erase(st.find(s[i]));
        fsum -= f[i];
    }
    cout << mn << NL;
}
