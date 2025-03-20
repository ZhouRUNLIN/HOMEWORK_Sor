<template>
  <NavBar />
  <div class="reader">
    <div class="search-bar">
      <input v-model="searchWord" placeholder="Enter word to search" class="search-input" />
      <button @click="search" class="btn">Search</button>
      <button @click="preview" class="btn" :disabled="currentMatchIndex <= 0">Preview</button>
      <button @click="next" class="btn" :disabled="currentMatchIndex >= matches.length - 1">Next</button>
    </div>
    <div v-if="matches.length > 0" class="match-info">
      Match {{ currentMatchIndex + 1 }} of {{ matches.length }}:
      {{ matches[currentMatchIndex] || "" }}
    </div>
    <div class="book-content" ref="bookContent">
      <div
        v-for="(line, index) in formattedContents"
        :key="index"
        :class="{'highlighted-line': isLineHighlighted(index)}"
      >
        <span v-html="formatLineWithHighlight(line)"></span>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import NavBar from "../NavBar.vue";

export default {
  components: {NavBar},
  data() {
    return {
      searchWord: "",
      matches: [],
      currentMatchIndex: -1,
      bookContents: [],
      formattedContents: [],
      username: localStorage.getItem("username"),
      bookId: this.$route.params.bookId,
      bookURL: "",
    };
  },
  computed: {
    highlightedText() {
      return this.matches.length > 0 && this.currentMatchIndex !== -1
        ? this.matches[this.currentMatchIndex].split(": ")[1]
        : "";
    },
  },
  methods: {
    async loadBookContent() {
      try {
        const response = await axios.get("/api/bmr/project/v1/readBook", {
          params: {
            username: this.username,
            bookId: this.bookId,
          },
        });
        if (response.data.code === "0") {
          this.bookContents = response.data.data.contents;
          this.bookURL = response.data.data.bookURL;
          this.formattedContents = this.bookContents.map((content, index) => `${index + 1}: ${content}`);
        } else {
          alert(response.data.message);
        }
      } catch (error) {
        console.error("Failed to load book content", error);
      }
    },
    async search() {
      if (!this.searchWord.trim()) {
        alert("Please enter a word to search.");
        return;
      }
      if (!this.bookURL) {
        alert("Book URL is missing.");
        return;
      }
      try {
        const response = await axios.get(
          "/api/bmr/project/v1/bookmark/textInternalsearchBykmp",
          {
            params: {
              URL: this.bookURL,
              word: this.searchWord,
            },
          }
        );
        if (response.data.code === "0") {
          this.matches = response.data.data;
          this.currentMatchIndex = this.matches.length > 0 ? 0 : -1;
          if (this.matches.length === 0) {
            alert("No matches found.");
          } else {
            this.scrollToCurrentMatch();
          }
        } else {
          alert(response.data.message);
        }
      } catch (error) {
        console.error("Search failed", error);
      }
    },
    preview() {
      if (this.currentMatchIndex > 0) {
        this.currentMatchIndex--;
        this.scrollToCurrentMatch();
      }
    },
    next() {
      if (this.currentMatchIndex < this.matches.length - 1) {
        this.currentMatchIndex++;
        this.scrollToCurrentMatch();
      }
    },
    isLineHighlighted(index) {
      return (
        this.matches.length > 0 &&
        this.matches[this.currentMatchIndex]?.startsWith(`${index + 1}:`)
      );
    },
    formatLineWithHighlight(line) {
      if (!this.highlightedText) return line;
      const regex = new RegExp(`(${this.highlightedText})`, "gi");
      return line.replace(regex, '<span class="highlight">$1</span>');
    },
    scrollToCurrentMatch() {
      this.$nextTick(() => {
        const highlightedElement = this.$refs.bookContent.querySelector(
          ".highlighted-line"
        );
        if (highlightedElement) {
          highlightedElement.scrollIntoView({ behavior: "smooth", block: "center" });
        }
      });
    },
  },
  mounted() {
    this.loadBookContent();
  },
};


</script>

<style scoped>
.reader {
  padding: 20px;
  font-family: Arial, sans-serif;
}
.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
.search-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.btn {
  padding: 10px 15px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
.match-info {
  font-size: 14px;
  margin-bottom: 10px;
  color: #555;
}
.book-content {
  white-space: pre-wrap;
  font-size: 16px;
  line-height: 1.5;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 10px;
  max-height: 800px;
  overflow-y: auto;
}

.highlighted-line {
  background-color: #f0f8ff;
}
.highlight {
  background-color: yellow;
  font-weight: bold;
}
</style>
