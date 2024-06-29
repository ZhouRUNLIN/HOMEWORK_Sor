#include <vector>
#include <forward_list>
#include <cstddef>

template <typename K, typename V>
class HashTable {
private:
    struct Entry {
        const K key;
        V value;

        Entry(const K& k, const V& v) : key(k), value(v) {}
    };

    std::vector<std::forward_list<Entry>> buckets;

public:
    HashTable(size_t size) : buckets(size) {}

    const V* get(const K& key) const {
        size_t h = std::hash<K>()(key);
        size_t index = h % buckets.size();

        for (const Entry& entry : buckets[index]) {
            if (entry.key == key) {
                return &entry.value;
            }
        }

        return nullptr;
    }

    bool put(const K& key, const V& value) {
        size_t h = std::hash<K>()(key);
        size_t index = h % buckets.size();

        for (Entry& entry : buckets[index]) {
            if (entry.key == key) {
                entry.value = value;
                return true;
            }
        }

        buckets[index].push_front(Entry(key, value));
        return false;
    }


    size_t size() const {
        size_t count = 0;
        for (const auto& bucket : buckets) {
            for (const auto& entry : bucket) {
                count++;
            }
        }
        return count;
    }

    class iterator {
    private:
        typedef typename std::vector<std::forward_list<Entry>>::iterator bucket_iterator;
        typedef typename std::forward_list<Entry>::iterator entry_iterator;

        bucket_iterator vit;
        entry_iterator lit;
        bucket_iterator vend;

    public:
        iterator(bucket_iterator vit, bucket_iterator vend)
                : vit(vit), vend(vend), lit(vit->begin()) {
            while (vit != vend && lit == vit->end()) {
                ++vit;
                if (vit != vend) {
                    lit = vit->begin();
                }
            }
        }

        iterator& operator++() {
            ++lit;
            while (vit != vend && lit == vit->end()) {
                ++vit;
                if (vit != vend) {
                    lit = vit->begin();
                }
            }
            return *this;
        }

        Entry& operator*() { return *lit; }
        Entry* operator->() { return &(*lit); }

        bool operator!=(const iterator& other) const {
            return vit != other.vit || (vit != vend && lit != other.lit);
        }
    };

    iterator begin() { return iterator(buckets.begin(), buckets.end()); }
    iterator end() { return iterator(buckets.end(), buckets.end()); }

    };



